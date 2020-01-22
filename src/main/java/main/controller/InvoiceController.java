package main.controller;

import main.entity.Invoice;
import main.entity.enums.OperationType;
import main.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping("/invoice")
    public ResponseEntity addProductToWarehouse(
            @Valid @RequestBody Invoice invoice,
            @RequestParam("operationType") OperationType operationType) {
        if (operationType.equals(OperationType.RECEIPT)) {
            invoiceService.addProductToWareHouse(invoice, operationType);
            return invoice == null ?
                    new ResponseEntity(null, HttpStatus.NOT_FOUND) :
                    new ResponseEntity(invoice, HttpStatus.OK);
        }
        invoiceService.writeOffProductFromWareHouse(invoice, operationType);
        return invoice == null ?
                new ResponseEntity(null, HttpStatus.NOT_FOUND) : new ResponseEntity(invoice, HttpStatus.OK);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity getInformationAboutReceiptInvoice(
            @PathVariable int id,
            @RequestParam("operationType") OperationType operationType) {
        if (operationType.equals(OperationType.RECEIPT)) {
            Invoice invoice = invoiceService.getInformationAboutReceiptInvoice(id, operationType);
            return invoice == null ?
                    new ResponseEntity(null, HttpStatus.BAD_REQUEST) : new ResponseEntity(invoice, HttpStatus.OK);
        }
        Invoice invoice = invoiceService.getInformationAboutWriteOffInvoce(id, operationType);
        return invoice == null ?
                new ResponseEntity(null, HttpStatus.BAD_REQUEST) : new ResponseEntity(invoice, HttpStatus.OK);
    }


    @PutMapping("/invoice/{id}")
    public ResponseEntity editReceiptInvoice(
            @Valid @RequestBody Invoice invoice, @PathVariable("id") int id,
            @RequestParam("operationType") OperationType operationType) {
        if (operationType.equals(OperationType.RECEIPT)) {
            Invoice newInvoice = invoiceService.editReceiptInvoice(id, invoice, operationType);
            return newInvoice == null ?
                    new ResponseEntity(null, HttpStatus.NOT_FOUND) : new ResponseEntity(newInvoice, HttpStatus.OK);
        }
        Invoice newInvoice = invoiceService.editWriteOffInvoice(id, invoice, operationType);
        return newInvoice == null ?
                new ResponseEntity(null, HttpStatus.NOT_FOUND) : new ResponseEntity(newInvoice, HttpStatus.OK);
    }


    @DeleteMapping("/invoice/{id}")
    public ResponseEntity deleteReceiptInvoice(
            @PathVariable("id") int id, @RequestParam("operationType") OperationType operationType) {
        if (operationType.equals(OperationType.RECEIPT)) {
            Invoice invoice = invoiceService.deleteReceiptInvoice(id, operationType);
            return invoice == null ?
                    new ResponseEntity(null, HttpStatus.BAD_REQUEST) :
                    new ResponseEntity(invoice, HttpStatus.NO_CONTENT);
        }
        Invoice invoice = invoiceService.deleteWriteOffInvoice(id, operationType);
        return invoice == null ?
                new ResponseEntity(null, HttpStatus.BAD_REQUEST) :
                new ResponseEntity(invoice, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/invoices")
    @ResponseBody
    public ResponseEntity getAllInvoicesByProduct(@Valid @RequestBody Invoice invoice) {
        List<Invoice> invoices = invoiceService.getAllInvoicesByProduct(invoice);
        return invoices.isEmpty() ?
                new ResponseEntity(null, HttpStatus.NOT_FOUND) : new ResponseEntity(invoices, HttpStatus.OK);
    }
}
