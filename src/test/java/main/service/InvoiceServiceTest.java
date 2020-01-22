package main.service;

import main.entity.Invoice;
import main.entity.Product;
import main.entity.enums.OperationType;
import main.repository.InvoiceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InvoiceServiceTest {

    private static Invoice invoice;

    @MockBean
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceService invoiceService;

    @Before
    public void init() {
        invoice = new Invoice();
        invoice.setId(1);
        invoice.setProduct(new Product(1, "notebook", "lenovo", "black"));
        invoice.setDate(new Date());
        invoice.setAmount(1000);
        Mockito.when(invoiceRepository.save(invoice)).thenReturn(invoice);
    }

    @Test
    public void addProductToWareHouse() {
        OperationType operationType = OperationType.RECEIPT;
        invoiceService.addProductToWareHouse(invoice, operationType);
        Assert.assertNotNull(invoice);
        Assert.assertEquals(invoice.getOperationType(), OperationType.RECEIPT);
        assertNotNull(invoiceService.addProductToWareHouse(invoice, operationType));
        Mockito.verify(invoiceRepository, Mockito.times(2)).save(invoice);
    }

    @Test
    public void checkNullAddProduct() {
        OperationType operationType = OperationType.WRITEOFF;
        invoiceService.addProductToWareHouse(invoice, operationType);
        assertNull(invoiceService.addProductToWareHouse(invoice, operationType));
    }

    @Test
    public void writeOffProductFromWareHouse() {
        OperationType operationType = OperationType.WRITEOFF;
        invoiceService.writeOffProductFromWareHouse(invoice, operationType);
        Assert.assertNotNull(invoice);
        Assert.assertEquals(invoice.getOperationType(), OperationType.WRITEOFF);
        assertNotNull(invoiceService.writeOffProductFromWareHouse(invoice, operationType));
        Mockito.verify(invoiceRepository, Mockito.times(2)).save(invoice);
    }

    @Test
    public void checkNullWriteOffProduct() {
        OperationType operationType = OperationType.RECEIPT;
        invoiceService.writeOffProductFromWareHouse(invoice, operationType);
        assertNull(invoiceService.writeOffProductFromWareHouse(invoice, operationType));
    }

    @Test
    public void getInformationAboutReceiptInvoice() {
        Mockito.when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.ofNullable(invoice));
        OperationType operationType = OperationType.RECEIPT;
        Invoice newInvoice = invoiceService.getInformationAboutReceiptInvoice(invoice.getId(), operationType);
        Assert.assertNotNull(newInvoice);
        Assert.assertEquals(invoice.getId(), 1);
        Mockito.verify(invoiceRepository, Mockito.times(1)).findById(invoice.getId());
        Mockito.verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    public void checkNullInformationReceiptInvoice() {
        Mockito.when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.ofNullable(invoice));
        OperationType operationType = OperationType.WRITEOFF;
        invoiceService.getInformationAboutReceiptInvoice(invoice.getId(), operationType);
        Assert.assertNull(invoiceService.getInformationAboutReceiptInvoice(invoice.getId(), operationType));
    }

    @Test
    public void getInformationAboutWriteOffInvoce() {
        Mockito.when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.ofNullable(invoice));
        OperationType operationType = OperationType.WRITEOFF;
        Invoice newInvoice = invoiceService.getInformationAboutWriteOffInvoce(invoice.getId(), operationType);
        Assert.assertNotNull(newInvoice);
        Assert.assertEquals(invoice.getId(), 1);
        Mockito.verify(invoiceRepository, Mockito.times(1)).findById(invoice.getId());
        Mockito.verifyNoMoreInteractions(invoiceRepository);
    }

    @Test
    public void checkNullInformationWriteOffInvoice() {
        Mockito.when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.ofNullable(invoice));
        OperationType operationType = OperationType.RECEIPT;
        invoiceService.getInformationAboutWriteOffInvoce(invoice.getId(), operationType);
        Assert.assertNull(invoiceService.getInformationAboutWriteOffInvoce(invoice.getId(), operationType));
    }

    @Test
    public void editReceiptInvoice() {
        Mockito.when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.ofNullable(invoice));
        OperationType operationType = OperationType.RECEIPT;
        invoice.setOperationType(operationType);
        Invoice newInvoice = new Invoice();
        newInvoice.setId(2);
        newInvoice.setAmount(500);
        invoiceService.editReceiptInvoice(invoice.getId(), newInvoice, operationType);
        Assert.assertEquals(2, invoice.getId());
        Assert.assertEquals(500, invoice.getAmount());
    }

    @Test
    public void editWriteOffInvoice() {
        Mockito.when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.ofNullable(invoice));
        OperationType operationType = OperationType.WRITEOFF;
        invoice.setOperationType(operationType);
        Invoice newInvoice = new Invoice();
        newInvoice.setId(2);
        newInvoice.setAmount(500);
        invoiceService.editWriteOffInvoice(invoice.getId(), newInvoice, operationType);
        Assert.assertEquals(2, invoice.getId());
        Assert.assertEquals(500, invoice.getAmount());
    }

    @Test
    public void deleteReceiptInvoice() {
        OperationType operationType = OperationType.RECEIPT;
        Mockito.when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.of(invoice));
        Mockito.doNothing().when(invoiceRepository).deleteById(invoice.getId());
        invoiceService.deleteReceiptInvoice(invoice.getId(), operationType);
        Mockito.verify(invoiceRepository, Mockito.times(1)).findById(invoice.getId());
        Mockito.verify(invoiceRepository, Mockito.times(1)).deleteById(invoice.getId());
    }

    @Test
    public void deleteWriteOffInvoice() {
        OperationType operationType = OperationType.WRITEOFF;
        Mockito.when(invoiceRepository.findById(invoice.getId())).thenReturn(java.util.Optional.of(invoice));
        Mockito.doNothing().when(invoiceRepository).deleteById(invoice.getId());
        invoiceService.deleteWriteOffInvoice(invoice.getId(), operationType);
        Mockito.verify(invoiceRepository, Mockito.times(1)).findById(invoice.getId());
        Mockito.verify(invoiceRepository, Mockito.times(1)).deleteById(invoice.getId());
    }

    @Test
    public void getAllInvoicesByProduct() {
        Invoice invoice1 = new Invoice();
        invoice1.setId(1);
        invoice1.setAmount(100);
        invoice1.setOperationType(OperationType.RECEIPT);
        invoice1.setProduct(new Product(1, "notebook", "lenovo", "black"));
        Invoice invoice2 = new Invoice();
        invoice2.setProduct(new Product(3, "notebook", "microsoft", "white"));
        invoice2.setId(2);
        invoice2.setAmount(200);
        invoice2.setOperationType(OperationType.WRITEOFF);
        List<Invoice> expected = new ArrayList<>();
        expected.add(invoice1);
        expected.add(invoice2);
        Mockito.when(invoiceRepository.findAllByProductName(invoice1.getProduct().getName())).thenReturn(expected);
        List<Invoice> actual = invoiceService.getAllInvoicesByProduct(invoice1);
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
        Mockito.verify(invoiceRepository, Mockito.times(1))
                .findAllByProductName(invoice1.getProduct().getName());
        Mockito.verifyNoMoreInteractions(invoiceRepository);
        Assert.assertNotEquals(expected.get(0).getProduct().getShortTitle(), actual.get(1).getProduct().getShortTitle());
        Assert.assertEquals(expected.get(0).getProduct().getName(), actual.get(1).getProduct().getName());
    }
}