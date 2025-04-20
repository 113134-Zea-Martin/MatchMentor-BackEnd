package com.scaffold.template.config;

import com.scaffold.template.entities.InterestEntity;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.entities.UserInterestEntity;
import com.scaffold.template.repositories.InterestRepository;
import com.scaffold.template.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder, InterestRepository interestRepository) {
        return args -> {
            // Verificar si ya existen usuarios para evitar duplicados
            if (userRepository.count() == 0) {

                // Crear intereses
                InterestEntity interest1 = new InterestEntity();
                interest1.setName("Desarrollo Web");
                interest1.setDescription("Interés en el desarrollo de aplicaciones web modernas y eficientes.");
                InterestEntity interest2 = new InterestEntity();
                interest2.setName("Inteligencia Artificial");
                interest2.setDescription("Interés en el desarrollo de sistemas inteligentes y aprendizaje automático.");
                InterestEntity interest3 = new InterestEntity();
                interest3.setName("Desarrollo Móvil");
                interest3.setDescription("Interés en la creación de aplicaciones móviles innovadoras.");
                InterestEntity interest4 = new InterestEntity();
                interest4.setName("Ciberseguridad");
                interest4.setDescription("Interés en la protección de sistemas y datos contra amenazas cibernéticas.");
                InterestEntity interest5 = new InterestEntity();
                interest5.setName("Arquitectura de Software");
                interest5.setDescription("Interés en el diseño y estructura de sistemas de software complejos.");
                InterestEntity interest6 = new InterestEntity();
                interest6.setName("Microservicios");
                interest6.setDescription("Interés en la arquitectura de microservicios y su implementación.");
                InterestEntity interest7 = new InterestEntity();
                interest7.setName("Gestión de Proyectos");
                interest7.setDescription("Interés en la planificación y ejecución de proyectos de software.");
                InterestEntity interest8 = new InterestEntity();
                interest8.setName("DevOps");
                interest8.setDescription("Interés en la integración de desarrollo y operaciones para mejorar la entrega de software.");
                InterestEntity interest9 = new InterestEntity();
                interest9.setName("Big Data");
                interest9.setDescription("Interés en el análisis y procesamiento de grandes volúmenes de datos.");

                // Guardar intereses en la base de datos
                List<InterestEntity> interests = Arrays.asList(interest1, interest2, interest3, interest4, interest5,
                        interest6, interest7, interest8, interest9);
                interestRepository.saveAll(interests);

                // Crear usuario administrador
                UserEntity admin = new UserEntity();
                admin.setFirstName("Admin");
                admin.setLastName("Sistema");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("password123"));
                admin.setBirthDate(LocalDate.of(1990, 1, 15));
                admin.setLocation("Ciudad Administrativa");
                admin.setLinkedinUrl("https://linkedin.com/in/admin-sistema");
                admin.setBio("Administrador del sistema con experiencia en gestión de plataformas educativas");
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                admin.setCreatedAt(LocalDateTime.now());

                // Intereses para el admin
                UserInterestEntity adminInterest1 = new UserInterestEntity();
                adminInterest1.setUser(admin);
                adminInterest1.setInterest(interest1);
                UserInterestEntity adminInterest2 = new UserInterestEntity();
                adminInterest2.setUser(admin);
                adminInterest2.setInterest(interest2);
                UserInterestEntity adminInterest3 = new UserInterestEntity();
                adminInterest3.setUser(admin);
                adminInterest3.setInterest(interest3);

                admin.setUserInterests(Arrays.asList(adminInterest1, adminInterest2, adminInterest3));
                userRepository.save(admin);

                // Crear un usuario estudiante
                UserEntity student = new UserEntity();
                student.setFirstName("Usuario");
                student.setLastName("Normal");
                student.setEmail("martincho370@gmail.com");
                student.setPassword(passwordEncoder.encode("password123"));
                student.setBirthDate(LocalDate.of(1995, 5, 20));
                student.setLocation("Buenos Aires");
                student.setEducationLevel("Universitario");
                student.setStudyArea("Ingeniería de Software");
                student.setInstitution("Universidad de Buenos Aires");
                student.setGraduationYear("2023");
                student.setMentoringGoals("Aprender desarrollo web y buenas prácticas de programación");
                student.setLinkedinUrl("https://linkedin.com/in/usuario-normal");
                student.setBio("Estudiante de ingeniería buscando oportunidades para crecer profesionalmente");
                student.setRole(Role.STUDENT);
                student.setIsActive(true);
                student.setCreatedAt(LocalDateTime.now());

                // Intereses para el estudiante
                UserInterestEntity studentInterest1 = new UserInterestEntity();
                studentInterest1.setUser(student);
                studentInterest1.setInterest(interest4);
                UserInterestEntity studentInterest2 = new UserInterestEntity();
                studentInterest2.setUser(student);
                studentInterest2.setInterest(interest5);
                UserInterestEntity studentInterest3 = new UserInterestEntity();
                studentInterest3.setUser(student);
                studentInterest3.setInterest(interest6);

                student.setUserInterests(Arrays.asList(studentInterest1, studentInterest2, studentInterest3));
                userRepository.save(student);

                // Crear un tutor
                UserEntity tutor = new UserEntity();
                tutor.setFirstName("Tutor");
                tutor.setLastName("Experto");
                tutor.setEmail("tutor@example.com");
                tutor.setPassword(passwordEncoder.encode("password123"));
                tutor.setBirthDate(LocalDate.of(1985, 10, 8));
                tutor.setLocation("Córdoba");
                tutor.setCurrentProfession("Arquitecto de Software");
                tutor.setCompany("TechSolutions S.A.");
                tutor.setYearsOfExperience(10);
                tutor.setProfessionalBio("Experto en desarrollo de aplicaciones empresariales con Spring Boot y Angular");
                tutor.setHourlyRate(45.0);
                tutor.setLinkedinUrl("https://linkedin.com/in/tutor-experto");
                tutor.setBio("Apasionado por compartir conocimientos y ayudar a nuevos desarrolladores");
                tutor.setRole(Role.TUTOR);
                tutor.setIsActive(true);
                tutor.setCreatedAt(LocalDateTime.now());

                // Intereses para el tutor
                UserInterestEntity tutorInterest1 = new UserInterestEntity();
                tutorInterest1.setUser(tutor);
                tutorInterest1.setInterest(interest7);
                UserInterestEntity tutorInterest2 = new UserInterestEntity();
                tutorInterest2.setUser(tutor);
                tutorInterest2.setInterest(interest8);
                UserInterestEntity tutorInterest3 = new UserInterestEntity();
                tutorInterest3.setUser(tutor);
                tutorInterest3.setInterest(interest9);

                tutor.setUserInterests(Arrays.asList(tutorInterest1, tutorInterest2, tutorInterest3));
                userRepository.save(tutor);

                // Crear un usuario inactivo
                UserEntity userInactivo = new UserEntity();
                userInactivo.setFirstName("Usuario");
                userInactivo.setLastName("Inactivo");
                userInactivo.setEmail("inactivo@example.com");
                userInactivo.setPassword(passwordEncoder.encode("password123"));
                userInactivo.setBirthDate(LocalDate.of(1992, 3, 15));
                userInactivo.setLocation("Rosario");
                userInactivo.setEducationLevel("Posgrado");
                userInactivo.setStudyArea("Ciencias de la Computación");
                userInactivo.setInstitution("Universidad Nacional de Rosario");
                userInactivo.setGraduationYear("2020");
                userInactivo.setMentoringGoals("Perfeccionar habilidades en desarrollo móvil");
                userInactivo.setLinkedinUrl("https://linkedin.com/in/usuario-inactivo");
                userInactivo.setBio("Profesional en pausa buscando nuevas oportunidades");
                userInactivo.setRole(Role.STUDENT);
                userInactivo.setIsActive(false);
                userInactivo.setCreatedAt(LocalDateTime.now());

                // Intereses para el usuario inactivo
//                UserInterestEntity inactivoInterest = new UserInterestEntity();
//                inactivoInterest.setUser(userInactivo);
////                inactivoInterest.setInterestName("Desarrollo Móvil");
//
//                userInactivo.setUserInterests(Arrays.asList(inactivoInterest));
                userRepository.save(userInactivo);

                System.out.println("Datos iniciales cargados correctamente");
            }
        };
    }
}