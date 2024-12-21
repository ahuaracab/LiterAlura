package com.alura.literalura.repository;

import com.alura.literalura.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // Aquí puedes definir métodos de consulta personalizados si es necesario
}
