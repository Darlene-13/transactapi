package io.github.darlene.transactapi.exception;

public class DuplicateTransferException extends RuntimeException {
    public DuplicateTransferException(String reference) {
        super("Transfer with reference " + reference + " already processed. Duplicate rejected.");
    }
}