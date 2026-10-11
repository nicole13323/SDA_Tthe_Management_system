package com.sdatithe.repo;

import com.sdatithe.model.ReceiptDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceiptDocumentRepository extends MongoRepository<ReceiptDocument, String> {}
