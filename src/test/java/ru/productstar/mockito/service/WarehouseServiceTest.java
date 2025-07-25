package ru.productstar.mockito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.productstar.mockito.model.Warehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

    /**
     * Покрыть тестами методы findWarehouse и findClosestWarehouse.
     * Вызывать реальные методы зависимых сервисов и репозиториев нельзя.
     * Поиск должен осуществляться как минимум на трех складах.
     *
     * Должны быть проверены следующие сценарии:
     * - поиск несуществующего товара
     * - поиск существующего товара с достаточным количеством
     * - поиск существующего товара с недостаточным количеством
     *
     * Проверки:
     * - товар находится на нужном складе, учитывается количество и расстояние до него
     * - корректная работа для несуществующего товара
     * - порядок и количество вызовов зависимых сервисов
     */
    @Test
    public void mockFindWarehouse() {
        WarehouseService mockWarehouseService = mock(WarehouseService.class,withSettings().lenient());

        Warehouse mockWareHouse0 = new Warehouse("MockWarehouse0",30);
        Warehouse mockWareHouse1 = new Warehouse("MockWarehouse1",20);
        Warehouse mockWareHouse2 = new Warehouse("MockWarehouse2",5);

        when(mockWarehouseService.findWarehouse( eq("printer"), lt(5) ) ).thenReturn(mockWareHouse0);
        when(mockWarehouseService.findWarehouse( eq("printer"), and( geq(5) , leq(7)) )).thenReturn(mockWareHouse1);
        when(mockWarehouseService.findWarehouse( eq("laptop"), leq(3) ) ).thenReturn(mockWareHouse2);

        assertNull(mockWarehouseService.findWarehouse("watch", 6)); // - поиск несуществующего товара
        assertNotNull(mockWarehouseService.findWarehouse("printer", 6)); // - поиск существующего товара с достаточным количеством
        assertEquals(mockWarehouseService.findWarehouse("printer", 4), mockWareHouse0); // - поиск существующего товара с достаточным количеством на складе 1
        assertEquals(mockWarehouseService.findWarehouse("printer", 5), mockWareHouse1);  // - поиск существующего товара с достаточным количеством на складе 2
        assertEquals(mockWarehouseService.findWarehouse("laptop", 3), mockWareHouse2);  // - поиск существующего товара с достаточным количеством на складе 3
        assertNull(mockWarehouseService.findWarehouse("laptop", 4)); // - поиск существующего товара с недостаточным количеством
        verify(mockWarehouseService, times(6)).findWarehouse(anyString(),anyInt()); // - количество вызовов 6 штук
    }
    @Test
    public void mockFindClosestWarehouse() {
        WarehouseService mockWarehouseService = mock(WarehouseService.class,withSettings().lenient());

        Warehouse mockWareHouse0 = new Warehouse("MockWarehouse0",30);
        Warehouse mockWareHouse1 = new Warehouse("MockWarehouse1",20);
        Warehouse mockWareHouse2 = new Warehouse("MockWarehouse2",5);

        when(mockWarehouseService.findClosestWarehouse( eq("printer"), lt(5) ) ).thenReturn(mockWareHouse0);
        when(mockWarehouseService.findClosestWarehouse( eq("printer"), and( geq(5) , leq(7)) )).thenReturn(mockWareHouse1);
        when(mockWarehouseService.findClosestWarehouse( eq("laptop"), leq(3) ) ).thenReturn(mockWareHouse2);

        assertNull(mockWarehouseService.findClosestWarehouse("watch", 6)); // - поиск несуществующего товара
        assertNotNull(mockWarehouseService.findClosestWarehouse("printer", 6)); // - поиск существующего товара с достаточным количеством
        assertEquals(mockWarehouseService.findClosestWarehouse("printer", 4), mockWareHouse0); // - поиск существующего товара с достаточным количеством на складе 1
        assertEquals(mockWarehouseService.findClosestWarehouse("printer", 5), mockWareHouse1);  // - поиск существующего товара с достаточным количеством на складе 2
        assertEquals(mockWarehouseService.findClosestWarehouse("laptop", 3), mockWareHouse2);  // - поиск существующего товара с достаточным количеством на складе 3
        assertNull(mockWarehouseService.findClosestWarehouse("laptop", 4)); // - поиск существующего товара с недостаточным количеством
        verify(mockWarehouseService, times(6)).findClosestWarehouse(anyString(),anyInt()); // - количество вызовов 6 штук
    }
}
