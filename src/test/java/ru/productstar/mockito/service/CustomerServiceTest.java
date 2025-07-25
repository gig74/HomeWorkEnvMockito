package ru.productstar.mockito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.productstar.mockito.model.Customer;
import ru.productstar.mockito.repository.CustomerRepository;
import ru.productstar.mockito.repository.InitRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    /**
     * Тест 1 - Получение покупателя "Ivan"
     * Проверки:
     * - очередность и точное количество вызовов каждого метода из CustomerRepository
     * <p>
     * Тест 2 - Получение покупателя "Oleg"
     * Проверки:
     * - очередность и точное количество вызовов каждого метода из CustomerRepository
     * - в метод getOrCreate была передана строка "Oleg"
     */
    @Captor
    private ArgumentCaptor<String> captor;
    @Test
    public void mockGetOrCreate() {
        CustomerRepository spyCustomerRepository = spy(InitRepository.getInstance().getCustomerRepository());
        CustomerService spyCustomerService = spy(new CustomerService(spyCustomerRepository));
        Customer customer01 = spyCustomerService.getOrCreate("Ivan");
        Customer customer02 = spyCustomerService.getOrCreate("Oleg");

        // Очерёдность вызовов каждого метода из CustomerRepository
        InOrder inOrder = inOrder(spyCustomerService,spyCustomerRepository);
        inOrder.verify(spyCustomerRepository).getByName(anyString());
        inOrder.verify(spyCustomerRepository).getByName("Oleg"); // - в метод getOrCreate во второй раз была передана строка "Oleg"
        inOrder.verify(spyCustomerRepository).add(any());

        verify(spyCustomerRepository, times(1)).getByName("Oleg"); // - в метод getOrCreate один раз была передана строка "Oleg"
        verify(spyCustomerRepository, times(2)).getByName(anyString()); // - проверка что метод getByName вызывался 2 раза
        verify(spyCustomerRepository, times(1)).add(any()); // - проверка что метод add вызывался 1 раз

        // "Выдёргивание" и проверка аргументов, с которыми вызывается метод
        verify(spyCustomerRepository,times(2)).getByName(captor.capture());
        List nameList = captor.getAllValues();
        assertEquals("Oleg", nameList.get(1)); // При втором обращении к методу getByName вызывался аргумент "Oleg"
    }
}
