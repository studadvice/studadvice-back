package com.stud.studadvice.repository.administrative;

import com.stud.studadvice.model.administrative.AdministrativeProcess;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdministrativeRepository extends MongoRepository<AdministrativeProcess, String> {
}

