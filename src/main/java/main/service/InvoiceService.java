package main.service;

import main.entity.Invoice;
import main.entity.enums.OperationType;
import main.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    public Invoice addProductToWareHouse(Invoice invoice, OperationType operationType) {
        if (operationType.equals(OperationType.RECEIPT)) {
            invoice.setOperationType(operationType);
            return invoiceRepository.save(invoice);
        }
        return null;
    }

    public Invoice writeOffProductFromWareHouse(Invoice invoice, OperationType operationType) {
        if (operationType.equals(OperationType.WRITEOFF)) {
            invoice.setOperationType(operationType);
            return invoiceRepository.save(invoice);
        }
        return null;
    }

    public Invoice getInformationAboutReceiptInvoice(int id, OperationType operationType) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if (optionalInvoice.isPresent() && operationType.equals(OperationType.RECEIPT)) {
            return optionalInvoice.get();
        }
        return null;
    }

    public Invoice getInformationAboutWriteOffInvoce(int id, OperationType operationType) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if (optionalInvoice.isPresent() && operationType.equals(OperationType.WRITEOFF)) {
            return optionalInvoice.get();
        }
        return null;
    }

    public Invoice editReceiptInvoice(int id, Invoice newInvoice, OperationType operationType) {
        if (operationType.equals(OperationType.RECEIPT)) {
            return invoiceRepository.findById(id)
                    .filter(invoice -> invoice.getOperationType() == (OperationType.RECEIPT))
                    .map(invoice -> {
                        invoice.setId(newInvoice.getId());
                        invoice.setProduct(newInvoice.getProduct());
                        invoice.setDate(newInvoice.getDate());
                        invoice.setAmount(newInvoice.getAmount());
                        return invoiceRepository.save(invoice);
                    }).orElseGet(() -> {
                        newInvoice.setId(id);
                        return invoiceRepository.save(newInvoice);
                    });
        }
        return null;
    }

    public Invoice editWriteOffInvoice(int id, Invoice newInvoice, OperationType operationType) {
        if (operationType.equals(OperationType.WRITEOFF)) {
            return invoiceRepository.findById(id)
                    .filter(invoice -> invoice.getOperationType().equals(OperationType.WRITEOFF))
                    .map(invoice -> {
                        invoice.setId(newInvoice.getId());
                        invoice.setProduct(newInvoice.getProduct());
                        invoice.setDate(newInvoice.getDate());
                        invoice.setAmount(newInvoice.getAmount());
                        return invoiceRepository.save(invoice);
                    }).orElseGet(() -> {
                        newInvoice.setId(id);
                        return invoiceRepository.save(newInvoice);
                    });
        }
        return null;
    }

    public Invoice deleteReceiptInvoice(int id, OperationType operationType) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if (optionalInvoice.isPresent() && operationType.equals(OperationType.RECEIPT)) {
            invoiceRepository.deleteById(optionalInvoice.get().getId());
            return optionalInvoice.get();
        }
        return null;
    }

    public Invoice deleteWriteOffInvoice(int id, OperationType operationType) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if (optionalInvoice.isPresent() && operationType.equals(OperationType.WRITEOFF)) {
            invoiceRepository.deleteById(optionalInvoice.get().getId());
            return optionalInvoice.get();
        }
        return null;
    }

    public List<Invoice> getAllInvoicesByProduct(Invoice invoice) {
        return new ArrayList<>(invoiceRepository.findAllByProductName(invoice.getProduct().getName()));
    }


}
