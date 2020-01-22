package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Invoice;
import main.entity.Product;
import main.entity.enums.OperationType;
import main.repository.InvoiceRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceControllerTest {

    private static final ObjectMapper om = new ObjectMapper();
    private static Invoice invoice;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    InvoiceRepository invoiceRepository;

    @Before
    public void init() {
        invoice = new Invoice();
        invoice.setId(1);
        invoice.setAmount(500);
        invoice.setProduct(new Product(1,"notebook","lenovo","black"));
        invoice.setDate(new Date());
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.of(invoice));
        doNothing().when(invoiceRepository).deleteById(invoice.getId());
    }

    @Test
    public void addProductToWarehouseReceiptInvoice() throws Exception {
        invoice.setOperationType(OperationType.RECEIPT);
        mockMvc.perform(post("/invoice/")
                .content(om.writeValueAsString(invoice))
                .param("operationType", String.valueOf(OperationType.RECEIPT))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
        Mockito.verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    public void addProductToWarehouseWriteOffInvoice() throws Exception {
        invoice.setOperationType(OperationType.WRITEOFF);
        mockMvc.perform(post("/invoice/")
                .content(om.writeValueAsString(invoice))
                .param("operationType", String.valueOf(OperationType.WRITEOFF))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
        Mockito.verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    public void getInformationAboutReceiptInvoice() throws Exception {
        mockMvc.perform(get("/invoice/{id}", 1)
                .content(om.writeValueAsString(invoice))
                .param("operationType", String.valueOf(OperationType.WRITEOFF)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));

        verify(invoiceRepository, times(1)).findById(invoice.getId());
    }

    @Test
    public void editReceiptInvoice() throws Exception {
        invoice.setOperationType(OperationType.RECEIPT);
        Invoice newInvoice = new Invoice();
        newInvoice.setId(2);
        newInvoice.setOperationType(OperationType.RECEIPT);
        newInvoice.setAmount(200);
        newInvoice.setDate(new Date());
        when(invoiceRepository.save(newInvoice)).thenReturn(newInvoice);
        mockMvc.perform(put("/invoice/{id}", 1)
                .content(om.writeValueAsString(newInvoice))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("operationType", String.valueOf(OperationType.WRITEOFF))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteReceiptInvoice() throws Exception {
        mockMvc.perform(delete("/invoice/{id}", 1)
                .content(om.writeValueAsString(invoice))
                .param("operationType", String.valueOf(OperationType.WRITEOFF)))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(invoiceRepository, times(1)).deleteById(1);
    }

   /* @Test
    public void getAllInvoicesByProduct() throws Exception {
        List<Invoice> invoices = Arrays.asList(
                new Invoice(1, OperationType.RECEIPT, new Product(1,"notebook","lenovo","black"), new Date(), 100),
                new Invoice(2, OperationType.RECEIPT, new Product(2,"notebook","lenovo","black"), new Date(), 250));
        when(invoiceRepository.findAllByProductName(invoice.getProduct().getName())).thenReturn(invoices);

        mockMvc.perform(get("/invoices")
        .content(om.writeValueAsString(invoices)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].amount", Matchers.is(100)))
                .andExpect(jsonPath("$[1].amount", Matchers.is(250)));
        verify(invoiceRepository, times(1)).findAllByProductName(invoice.getProduct().getName());
    }*/
}