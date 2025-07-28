package ru.productstar.mockito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.productstar.mockito.model.Product;
import ru.productstar.mockito.model.Stock;
import ru.productstar.mockito.model.Warehouse;
import ru.productstar.mockito.repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void mockFindWarehouseAndFindClosestWarehouse() {

        WarehouseRepository mockWarehouseRepository = mock(WarehouseRepository.class);
        WarehouseService spyWarehouseService = spy( new WarehouseService(mockWarehouseRepository));

        Warehouse mockWareHouse0 = new Warehouse("MockWarehouse0",30);
        Warehouse mockWareHouse1 = new Warehouse("MockWarehouse1",20);
        Warehouse mockWareHouse2 = new Warehouse("MockWarehouse2",5);

        mockWareHouse0.addStock(new Stock(new Product("phone"), 400, 5));
        mockWareHouse1.addStock(new Stock(new Product("phone"),  380, 2));
        mockWareHouse1.addStock(new Stock(new Product("laptop"), 850, 1));
        mockWareHouse2.addStock(new Stock(new Product("phone"), 450, 3));
        List<Warehouse> toMockWarehouses = new ArrayList<>(Arrays.asList(mockWareHouse0, mockWareHouse1, mockWareHouse2));

        when(mockWarehouseRepository.all()).thenReturn(toMockWarehouses); // определяем обращение к методу репозитария

        assertNull(spyWarehouseService.findWarehouse("watch", 6)); // - поиск несуществующего товара
        assertNull(spyWarehouseService.findWarehouse("laptop", 2)); // - поиск существующего товара с недостаточным количеством
        assertEquals(spyWarehouseService.findWarehouse("phone", 2),mockWareHouse0); // - поиск существующего товара с достаточным количеством
        assertEquals(spyWarehouseService.findWarehouse("laptop", 1),mockWareHouse1); // - поиск существующего товара с достаточным количеством
        assertEquals(spyWarehouseService.findClosestWarehouse("phone", 2),mockWareHouse2); // - поиск существующего товара с учетом расстояния до него

        // Для визуального контроля вызова зависимых сервисов
        System.out.println(mockingDetails(spyWarehouseService).printInvocations());
        System.out.println(mockingDetails(mockWarehouseRepository).printInvocations());

        verify(spyWarehouseService, times(4)).findWarehouse(anyString(),anyInt()); // - количество вызовов 3 раза
        verify(spyWarehouseService, times(1)).findClosestWarehouse(anyString(),anyInt()); // - количество вызовов 1 раз
        verify(mockWarehouseRepository, times(5)).all(); // - количество вызовов 5 раз

        // порядок вызовов зависимых сервисов
        InOrder inOrder = inOrder(spyWarehouseService, mockWarehouseRepository); //
        inOrder.verify(spyWarehouseService).findWarehouse(anyString(),anyInt());
        inOrder.verify(mockWarehouseRepository).all();
        inOrder.verify(spyWarehouseService).findWarehouse(anyString(),anyInt());
        inOrder.verify(mockWarehouseRepository).all();
        inOrder.verify(spyWarehouseService).findWarehouse(anyString(),anyInt());
        inOrder.verify(mockWarehouseRepository).all();
        inOrder.verify(spyWarehouseService).findClosestWarehouse(anyString(),anyInt());
        inOrder.verify(mockWarehouseRepository).all();
    }

}
