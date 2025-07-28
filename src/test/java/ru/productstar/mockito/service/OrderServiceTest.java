package ru.productstar.mockito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.productstar.mockito.ProductNotFoundException;
import ru.productstar.mockito.model.Order;
import ru.productstar.mockito.repository.InitRepository;
import ru.productstar.mockito.repository.OrderRepository;
import ru.productstar.mockito.repository.ProductRepository;
import ru.productstar.mockito.repository.WarehouseRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    /**
     * Покрыть тестами методы create и addProduct.
     * Можно использовать вызовы реальных методов.
     *
     * Должны быть проверены следующие сценарии:
     * - создание ордера для существующего и нового клиента
     * - добавление существующего и несуществующего товара
     * - добавление товара в достаточном и не достаточном количестве
     * - заказ товара с быстрой доставкой
     *
     * Проверки:
     * - общая сумма заказа соответствует ожидаемой
     * - корректная работа для несуществующего товара
     * - порядок и количество вызовов зависимых сервисов
     * - факт выбрасывания ProductNotFoundException
     */
    @Test
    public void mockCreateAndAddProduct() throws ProductNotFoundException {
        CustomerService spyCustomerService = spy(new CustomerService(InitRepository.getInstance().getCustomerRepository()) );
        WarehouseService spyWarehouseService = spy(new WarehouseService(InitRepository.getInstance().getWarehouseRepository()));
        OrderRepository spyOrderRepository = spy(InitRepository.getInstance().getOrderRepository());
        ProductRepository spyProductRepository = spy(InitRepository.getInstance().getProductRepository());

        OrderService spyOrderService = spy(new OrderService(spyCustomerService ,spyWarehouseService ,spyOrderRepository ,spyProductRepository ));

        Order orderOld = spyOrderService.create("Ivan"); // - создание ордера для существующего клиента
        Order orderNew = spyOrderService.create("Igor"); // - создание ордера для нового клиента (с быстрой доставкой)

        spyOrderService.addProduct(orderOld, "monitor", 5, false) ; // - добавление существующего товара в достаточном количестве
        spyOrderService.addProduct(orderOld, "phone", 5, false) ; // - добавление существующего товара в достаточном количестве
        spyOrderService.addProduct(orderNew, "keyboard", 5, true) ; // - добавление существующего товара в достаточном количестве с быстрой доставкой

        // Проверка: факт выбрасывания ProductNotFoundException
        Order finalOrderOld = orderOld;
        assertThrows(ProductNotFoundException.class, () -> spyOrderService.addProduct(finalOrderOld, "mouse", 5, false) ) ; // - добавление несуществующего товара
        assertThrows(ProductNotFoundException.class, () -> spyOrderService.addProduct(finalOrderOld, "phone", 15, false) ) ; // - добавление существующего товара в недостаточном количестве
        // Для визуального контроля вызова зависимых сервисов
        System.out.println(mockingDetails(spyCustomerService).printInvocations());
        System.out.println(mockingDetails(spyWarehouseService).printInvocations());
        System.out.println(mockingDetails(spyOrderRepository).printInvocations());
        System.out.println(mockingDetails(spyProductRepository).printInvocations());
        // Проверка количества вызовов зависимых сервисов
        verify(spyCustomerService, times(2)).getOrCreate(anyString()); // - количество вызовов зависимых сервисов
        verify(spyWarehouseService, times(4)).findWarehouse(anyString(),anyInt()); // - количество вызовов зависимых сервисов
        verify(spyWarehouseService, times(1)).findClosestWarehouse(anyString(),anyInt()); // - количество вызовов зависимых сервисов
        verify(spyWarehouseService, times(3)).getStock(any(),anyString()); // - количество вызовов зависимых сервисов
        verify(spyOrderRepository, times(2)).create(any()); // - количество вызовов зависимых сервисов
        verify(spyOrderRepository, times(3)).addDelivery(anyInt(),any()); // - количество вызовов зависимых сервисов
        verify(spyProductRepository, times(3)).getByName(anyString()); // - количество вызовов зависимых сервисов
        // порядок вызовов зависимых сервисов
        InOrder inOrder = inOrder(spyOrderService, spyCustomerService, spyWarehouseService, spyOrderRepository, spyProductRepository); //
        inOrder.verify(spyOrderService).create(anyString());
        inOrder.verify(spyCustomerService).getOrCreate(anyString());
        inOrder.verify(spyOrderService).create(anyString());
        inOrder.verify(spyCustomerService).getOrCreate(anyString());
        inOrder.verify(spyOrderService).addProduct(any(),anyString(),anyInt(),anyBoolean());
        inOrder.verify(spyWarehouseService).findWarehouse(anyString(),anyInt());
        inOrder.verify(spyProductRepository).getByName(anyString());
        inOrder.verify(spyWarehouseService).getStock(any(),anyString());
        inOrder.verify(spyOrderRepository).addDelivery(anyInt(),any());
        inOrder.verify(spyOrderService).addProduct(any(),anyString(),anyInt(),anyBoolean());
        inOrder.verify(spyWarehouseService).findWarehouse(anyString(),anyInt());
        inOrder.verify(spyProductRepository).getByName(anyString());
        inOrder.verify(spyWarehouseService).getStock(any(),anyString());
        inOrder.verify(spyOrderRepository).addDelivery(anyInt(),any());
        inOrder.verify(spyOrderService).addProduct(any(),anyString(),anyInt(),anyBoolean());
        inOrder.verify(spyWarehouseService).findClosestWarehouse(anyString(),anyInt());
        inOrder.verify(spyProductRepository).getByName(anyString());
        inOrder.verify(spyWarehouseService).getStock(any(),anyString());
        inOrder.verify(spyOrderRepository).addDelivery(anyInt(),any());
        inOrder.verify(spyOrderService).addProduct(any(),anyString(),anyInt(),anyBoolean());
        inOrder.verify(spyWarehouseService).findWarehouse(anyString(),anyInt());
        inOrder.verify(spyOrderService).addProduct(any(),anyString(),anyInt(),anyBoolean());
        inOrder.verify(spyWarehouseService).findWarehouse(anyString(),anyInt());

        assertEquals( 3500 , orderOld.getTotal() ); // проверка - общая сумма заказа соответствует ожидаемой
        assertEquals( 200 , orderNew.getTotal() ); // проверка - общая сумма заказа соответствует ожидаемой

        assertEquals(InitRepository.getInstance().getWarehouseRepository().getById(2) , orderNew.getDeliveries().get(0).getWarehouse() ); // - проверка "быстрой доставки" с ближайшего склада с id = 2

    }
}
