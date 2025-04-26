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
import java.util.ArrayList;

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

                // Crear estudiante 1
                UserEntity student1 = new UserEntity();
                student1.setFirstName("Martín");
                student1.setLastName("González");
                student1.setEmail("martincho370@gmail.com");
                student1.setPassword(passwordEncoder.encode("password123"));
                student1.setBirthDate(LocalDate.of(1995, 5, 20));
                student1.setLocation("Buenos Aires");
                student1.setEducationLevel("Universitario");
                student1.setStudyArea("Ingeniería de Software");
                student1.setInstitution("Universidad de Buenos Aires");
                student1.setGraduationYear("2023");
                student1.setMentoringGoals("Aprender desarrollo web y buenas prácticas de programación");
                student1.setLinkedinUrl("https://linkedin.com/in/martin-gonzalez");
                student1.setBio("Estudiante de ingeniería buscando oportunidades para crecer profesionalmente");
                student1.setRole(Role.STUDENT);
                student1.setIsActive(true);
                student1.setCreatedAt(LocalDateTime.now());

                // Intereses para el estudiante 1
                List<UserInterestEntity> student1Interests = new ArrayList<>();
                UserInterestEntity s1Interest1 = new UserInterestEntity();
                s1Interest1.setUser(student1);
                s1Interest1.setInterest(interest1); // Desarrollo Web
                student1Interests.add(s1Interest1);

                UserInterestEntity s1Interest2 = new UserInterestEntity();
                s1Interest2.setUser(student1);
                s1Interest2.setInterest(interest4); // Ciberseguridad
                student1Interests.add(s1Interest2);

                UserInterestEntity s1Interest3 = new UserInterestEntity();
                s1Interest3.setUser(student1);
                s1Interest3.setInterest(interest6); // Microservicios
                student1Interests.add(s1Interest3);

                student1.setUserInterests(student1Interests);
                userRepository.save(student1);

                // Crear estudiante 2
                UserEntity student2 = new UserEntity();
                student2.setFirstName("Ana");
                student2.setLastName("Perez");
                student2.setEmail("ana.perez@example.com");
                student2.setPassword(passwordEncoder.encode("password123"));
                student2.setBirthDate(LocalDate.of(1997, 8, 12));
                student2.setLocation("Mendoza");
                student2.setEducationLevel("Universitario");
                student2.setStudyArea("Ciencias de la Computación");
                student2.setInstitution("Universidad Nacional de Cuyo");
                student2.setGraduationYear("2024");
                student2.setMentoringGoals("Especialización en inteligencia artificial y desarrollo de modelos");
                student2.setLinkedinUrl("https://linkedin.com/in/ana-perez");
                student2.setBio("Apasionada por la IA y el machine learning");
                student2.setRole(Role.STUDENT);
                student2.setIsActive(true);
                student2.setCreatedAt(LocalDateTime.now());

                // Intereses para la estudiante 2
                List<UserInterestEntity> student2Interests = new ArrayList<>();
                UserInterestEntity s2Interest1 = new UserInterestEntity();
                s2Interest1.setUser(student2);
                s2Interest1.setInterest(interest2); // Inteligencia Artificial
                student2Interests.add(s2Interest1);

                UserInterestEntity s2Interest2 = new UserInterestEntity();
                s2Interest2.setUser(student2);
                s2Interest2.setInterest(interest9); // Big Data
                student2Interests.add(s2Interest2);

                UserInterestEntity s2Interest3 = new UserInterestEntity();
                s2Interest3.setUser(student2);
                s2Interest3.setInterest(interest5); // Arquitectura de Software
                student2Interests.add(s2Interest3);

                student2.setUserInterests(student2Interests);
                userRepository.save(student2);

                // Crear tutor 1
                UserEntity tutor1 = new UserEntity();
                tutor1.setFirstName("Carlos");
                tutor1.setLastName("Rodríguez");
                tutor1.setEmail("carlos.rodriguez@example.com");
                tutor1.setPassword(passwordEncoder.encode("password123"));
                tutor1.setBirthDate(LocalDate.of(1985, 10, 8));
                tutor1.setLocation("Córdoba");
                tutor1.setCurrentProfession("Arquitecto de Software");
                tutor1.setCompany("TechSolutions S.A.");
                tutor1.setYearsOfExperience(10);
                tutor1.setProfessionalBio("Experto en desarrollo de aplicaciones empresariales con Spring Boot y Angular");
                tutor1.setHourlyRate(45.0);
                tutor1.setLinkedinUrl("https://linkedin.com/in/carlos-rodriguez");
                tutor1.setBio("Apasionado por compartir conocimientos y ayudar a nuevos desarrolladores");
                tutor1.setRole(Role.TUTOR);
                tutor1.setIsActive(true);
                tutor1.setIsVisible(false);
                tutor1.setCreatedAt(LocalDateTime.now());

                // Intereses para el tutor 1
                List<UserInterestEntity> tutor1Interests = new ArrayList<>();
                UserInterestEntity t1Interest1 = new UserInterestEntity();
                t1Interest1.setUser(tutor1);
                t1Interest1.setInterest(interest1); // Desarrollo Web
                tutor1Interests.add(t1Interest1);

                UserInterestEntity t1Interest2 = new UserInterestEntity();
                t1Interest2.setUser(tutor1);
                t1Interest2.setInterest(interest5); // Arquitectura de Software
                tutor1Interests.add(t1Interest2);

                UserInterestEntity t1Interest3 = new UserInterestEntity();
                t1Interest3.setUser(tutor1);
                t1Interest3.setInterest(interest6); // Microservicios
                tutor1Interests.add(t1Interest3);

                tutor1.setUserInterests(tutor1Interests);
                userRepository.save(tutor1);

                // Crear tutor 2
                UserEntity tutor2 = new UserEntity();
                tutor2.setFirstName("Laura");
                tutor2.setLastName("Gómez");
                tutor2.setEmail("laura.gomez@example.com");
                tutor2.setPassword(passwordEncoder.encode("password123"));
                tutor2.setBirthDate(LocalDate.of(1983, 4, 15));
                tutor2.setLocation("Buenos Aires");
                tutor2.setCurrentProfession("Data Scientist");
                tutor2.setCompany("DataInsights");
                tutor2.setYearsOfExperience(8);
                tutor2.setProfessionalBio("Especialista en modelos predictivos e inteligencia artificial aplicada a negocios");
                tutor2.setHourlyRate(50.0);
                tutor2.setLinkedinUrl("https://linkedin.com/in/laura-gomez");
                tutor2.setBio("Enfocada en ayudar a estudiantes a incursionar en el mundo de los datos y la IA");
                tutor2.setRole(Role.TUTOR);
                tutor2.setIsActive(true);
                tutor2.setIsVisible(true);
                tutor2.setCreatedAt(LocalDateTime.now());

                // Intereses para el tutor 2
                List<UserInterestEntity> tutor2Interests = new ArrayList<>();
                UserInterestEntity t2Interest1 = new UserInterestEntity();
                t2Interest1.setUser(tutor2);
                t2Interest1.setInterest(interest2); // Inteligencia Artificial
                tutor2Interests.add(t2Interest1);

                UserInterestEntity t2Interest2 = new UserInterestEntity();
                t2Interest2.setUser(tutor2);
                t2Interest2.setInterest(interest9); // Big Data
                tutor2Interests.add(t2Interest2);

                UserInterestEntity t2Interest3 = new UserInterestEntity();
                t2Interest3.setUser(tutor2);
                t2Interest3.setInterest(interest7); // Gestión de Proyectos
                tutor2Interests.add(t2Interest3);

                tutor2.setUserInterests(tutor2Interests);
                userRepository.save(tutor2);

                // Crear tutor 3
                UserEntity tutor3 = new UserEntity();
                tutor3.setFirstName("Javier");
                tutor3.setLastName("Mendoza");
                tutor3.setEmail("javier.mendoza@example.com");
                tutor3.setPassword(passwordEncoder.encode("password123"));
                tutor3.setBirthDate(LocalDate.of(1988, 7, 22));
                tutor3.setLocation("Rosario");
                tutor3.setCurrentProfession("Ingeniero de Seguridad");
                tutor3.setCompany("SecureNet");
                tutor3.setYearsOfExperience(7);
                tutor3.setProfessionalBio("Experto en seguridad informática, pentesting y auditorías de seguridad");
                tutor3.setHourlyRate(40.0);
                tutor3.setLinkedinUrl("https://linkedin.com/in/javier-mendoza");
                tutor3.setBio("Dedicado a enseñar buenas prácticas de seguridad en el desarrollo de software");
                tutor3.setRole(Role.TUTOR);
                tutor3.setIsActive(true);
                tutor3.setIsVisible(true);
                tutor3.setCreatedAt(LocalDateTime.now());

                // Intereses para el tutor 3
                List<UserInterestEntity> tutor3Interests = new ArrayList<>();
                UserInterestEntity t3Interest1 = new UserInterestEntity();
                t3Interest1.setUser(tutor3);
                t3Interest1.setInterest(interest4); // Ciberseguridad
                tutor3Interests.add(t3Interest1);

                UserInterestEntity t3Interest2 = new UserInterestEntity();
                t3Interest2.setUser(tutor3);
                t3Interest2.setInterest(interest1); // Desarrollo Web
                tutor3Interests.add(t3Interest2);

                UserInterestEntity t3Interest3 = new UserInterestEntity();
                t3Interest3.setUser(tutor3);
                t3Interest3.setInterest(interest8); // DevOps
                tutor3Interests.add(t3Interest3);

                tutor3.setUserInterests(tutor3Interests);
                userRepository.save(tutor3);

                // Crear estudiante 3
                UserEntity student3 = new UserEntity();
                student3.setFirstName("Pedro");
                student3.setLastName("López");
                student3.setEmail("pedro.lopez@example.com");
                student3.setPassword(passwordEncoder.encode("password123"));
                student3.setBirthDate(LocalDate.of(1996, 3, 28));
                student3.setLocation("La Plata");
                student3.setEducationLevel("Universitario");
                student3.setStudyArea("Ingeniería en Sistemas");
                student3.setInstitution("Universidad Nacional de La Plata");
                student3.setGraduationYear("2023");
                student3.setMentoringGoals("Desarrollo de aplicaciones móviles y aprendizaje de nuevas tecnologías");
                student3.setLinkedinUrl("https://linkedin.com/in/pedro-lopez");
                student3.setBio("Estudiante entusiasta de la tecnología móvil y el desarrollo multiplataforma");
                student3.setRole(Role.STUDENT);
                student3.setIsActive(true);
                student3.setCreatedAt(LocalDateTime.now());

                // Intereses para el estudiante 3
                List<UserInterestEntity> student3Interests = new ArrayList<>();
                UserInterestEntity s3Interest1 = new UserInterestEntity();
                s3Interest1.setUser(student3);
                s3Interest1.setInterest(interest3); // Desarrollo Móvil
                student3Interests.add(s3Interest1);

                UserInterestEntity s3Interest2 = new UserInterestEntity();
                s3Interest2.setUser(student3);
                s3Interest2.setInterest(interest8); // DevOps
                student3Interests.add(s3Interest2);

                UserInterestEntity s3Interest3 = new UserInterestEntity();
                s3Interest3.setUser(student3);
                s3Interest3.setInterest(interest1); // Desarrollo Web
                student3Interests.add(s3Interest3);

                student3.setUserInterests(student3Interests);
                userRepository.save(student3);

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
                userRepository.save(userInactivo);

                System.out.println("Datos iniciales cargados correctamente");
            }
        };
    }
}