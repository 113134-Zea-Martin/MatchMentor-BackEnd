package com.scaffold.template.config;

import com.scaffold.template.entities.*;
import com.scaffold.template.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            InterestRepository interestRepository,
            MatchRepository matchRepository,
            NotificationRepository notificationRepository,
            UserViewedProfileRepository userViewedProfileRepository,
            MeetingRepository meetingRepository,
            PaymentRepository paymentRepository
    ) {
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
                InterestEntity interest10 = new InterestEntity();
                interest10.setName("Cloud Computing");
                interest10.setDescription("Interés en servicios y arquitecturas en la nube.");
                InterestEntity interest11 = new InterestEntity();
                interest11.setName("Testing Automatizado");
                interest11.setDescription("Interés en pruebas automáticas y calidad de software.");

                // Guardar intereses en la base de datos
                List<InterestEntity> interests = Arrays.asList(
                        interest1, interest2, interest3, interest4, interest5,
                        interest6, interest7, interest8, interest9, interest10, interest11
                );
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
                tutor1.setIsVisible(true);
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
                tutor3.setEmail("mzeacardenas@gmail.com");
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
                tutor3.setMercadoPagoToken("APP_USR-1481730718147445-051422-da44d4d99ae7069649fadfec9a22621c-2428775243");

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

                // ------------------ AGREGAR DATOS PARA PERFILES VISTOS ------------------
                UserViewedProfileEntity view1 = new UserViewedProfileEntity();
                view1.setViewer(student1);
                view1.setViewedUser(tutor1);
                view1.setStatus(Status.REJECTED);
                view1.setViewedAt(LocalDateTime.now().minusDays(5));
                userViewedProfileRepository.save(view1);

                UserViewedProfileEntity view2 = new UserViewedProfileEntity();
                view2.setViewer(student1);
                view2.setViewedUser(tutor2);
                view2.setStatus(Status.REJECTED);
                view2.setViewedAt(LocalDateTime.now().minusDays(4));
                userViewedProfileRepository.save(view2);

                UserViewedProfileEntity view3 = new UserViewedProfileEntity();
                view3.setViewer(student2);
                view3.setViewedUser(tutor2);
                view3.setStatus(Status.ACCEPTED);
                view3.setViewedAt(LocalDateTime.now().minusDays(3));
                userViewedProfileRepository.save(view3);

                UserViewedProfileEntity view4 = new UserViewedProfileEntity();
                view4.setViewer(student3);
                view4.setViewedUser(tutor3);
                view4.setStatus(Status.ACCEPTED);
                view4.setViewedAt(LocalDateTime.now().minusDays(2));
                userViewedProfileRepository.save(view4);

                // ------------------ AGREGAR DATOS PARA MATCHES ------------------
                MatchEntity match1 = new MatchEntity();
                match1.setStudent(student1);
                match1.setTutor(tutor1);
                match1.setStatus(Status.PENDING);
                match1.setCreatedAt(LocalDateTime.now().minusDays(5));
                matchRepository.save(match1);

                MatchEntity match2 = new MatchEntity();
                match2.setStudent(student2);
                match2.setTutor(tutor2);
                match2.setStatus(Status.ACCEPTED);
                match2.setCreatedAt(LocalDateTime.now().minusDays(3));
                match2.setUpdatedAt(LocalDateTime.now().minusDays(2));
                matchRepository.save(match2);

                MatchEntity match3 = new MatchEntity();
                match3.setStudent(student3);
                match3.setTutor(tutor3);
                match3.setStatus(Status.ACCEPTED);
                match3.setCreatedAt(LocalDateTime.now().minusDays(1));
                matchRepository.save(match3);

                MatchEntity match4 = new MatchEntity();
                match4.setStudent(student1);
                match4.setTutor(tutor3);
                match4.setStatus(Status.ACCEPTED);
                match4.setCreatedAt(LocalDateTime.now().minusDays(7));
                match4.setUpdatedAt(LocalDateTime.now().minusDays(6));
                matchRepository.save(match4);

                // ------------------ AGREGAR DATOS PARA NOTIFICACIONES ------------------
                NotificationEntity notification1 = new NotificationEntity();
                notification1.setUserId(tutor1.getId());
                notification1.setMessage("Martín González te ha enviado una solicitud de conexión");
                notification1.setNotificationType(NotificationType.NEW_CONNECTION_REQUEST);
                notification1.setRelatedEntityId(match1.getId());
                notification1.setIsRead(false);
                notification1.setCreatedAt(LocalDateTime.now().minusDays(5));
                notificationRepository.save(notification1);

                NotificationEntity notification2 = new NotificationEntity();
                notification2.setUserId(tutor3.getId());
                notification2.setMessage("Pedro López te ha enviado una solicitud de conexión");
                notification2.setNotificationType(NotificationType.NEW_CONNECTION_REQUEST);
                notification2.setRelatedEntityId(match3.getId());
                notification2.setIsRead(false);
                notification2.setCreatedAt(LocalDateTime.now().minusDays(1));
                notificationRepository.save(notification2);

                NotificationEntity notification3 = new NotificationEntity();
                notification3.setUserId(student2.getId());
                notification3.setMessage("Laura Gómez ha aceptado tu solicitud de conexión");
                notification3.setNotificationType(NotificationType.CONNECTION_ACCEPTED);
                notification3.setRelatedEntityId(match2.getId());
                notification3.setIsRead(true);
                notification3.setCreatedAt(LocalDateTime.now().minusDays(2));
                notificationRepository.save(notification3);

                NotificationEntity notification4 = new NotificationEntity();
                notification4.setUserId(student1.getId());
                notification4.setMessage("Javier Mendoza ha rechazado tu solicitud de conexión");
                notification4.setNotificationType((NotificationType.CONNECTION_REJECTED));
                notification4.setRelatedEntityId(match4.getId());
                notification4.setIsRead(true);
                notification4.setCreatedAt(LocalDateTime.now().minusDays(6));
                notificationRepository.save(notification4);

                // Meeting entre estudiante 1 y tutor 3
                MeetingEntity meeting = new MeetingEntity();
                meeting.setDate(LocalDate.now());
                meeting.setDuration(1);
                meeting.setHourlyRate(50.0);
                meeting.setTime(LocalTime.of(10, 0));
                meeting.setCreatedAt(LocalDateTime.now());
                meeting.setMatch(match4);
                meeting.setMentor(tutor3);
                meeting.setStudent(student1);
                meeting.setReason("Consulta sobre desarrollo de software");
                meeting.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
                meetingRepository.save(meeting);

                // ------------------ DATOS ADICIONALES ------------------

                // Estudiante 4
                UserEntity student4 = new UserEntity();
                student4.setFirstName("Lucía");
                student4.setLastName("Fernández");
                student4.setEmail("lucia.fernandez@example.com");
                student4.setPassword(passwordEncoder.encode("password123"));
                student4.setBirthDate(LocalDate.of(1998, 2, 10));
                student4.setLocation("Salta");
                student4.setEducationLevel("Universitario");
                student4.setStudyArea("Informática");
                student4.setInstitution("Universidad Nacional de Salta");
                student4.setGraduationYear("2025");
                student4.setMentoringGoals("Aprender sobre cloud y testing automatizado");
                student4.setLinkedinUrl("https://linkedin.com/in/lucia-fernandez");
                student4.setBio("Interesada en nuevas tecnologías y automatización");
                student4.setRole(Role.STUDENT);
                student4.setIsActive(true);
                student4.setCreatedAt(LocalDateTime.now());
                List<UserInterestEntity> student4Interests = new ArrayList<>();
                UserInterestEntity s4Interest1 = new UserInterestEntity();
                s4Interest1.setUser(student4);
                s4Interest1.setInterest(interest10); // Cloud
                student4Interests.add(s4Interest1);
                UserInterestEntity s4Interest2 = new UserInterestEntity();
                s4Interest2.setUser(student4);
                s4Interest2.setInterest(interest11); // Testing
                student4Interests.add(s4Interest2);
                student4.setUserInterests(student4Interests);
                userRepository.save(student4);

                // Estudiante 5
                UserEntity student5 = new UserEntity();
                student5.setFirstName("Sofía");
                student5.setLastName("Martínez");
                student5.setEmail("sofia.martinez@example.com");
                student5.setPassword(passwordEncoder.encode("password123"));
                student5.setBirthDate(LocalDate.of(1999, 11, 5));
                student5.setLocation("Santa Fe");
                student5.setEducationLevel("Universitario");
                student5.setStudyArea("Sistemas de Información");
                student5.setInstitution("UTN Santa Fe");
                student5.setGraduationYear("2024");
                student5.setMentoringGoals("Mejorar habilidades en DevOps y microservicios");
                student5.setLinkedinUrl("https://linkedin.com/in/sofia-martinez");
                student5.setBio("Apasionada por la automatización y la integración continua");
                student5.setRole(Role.STUDENT);
                student5.setIsActive(true);
                student5.setCreatedAt(LocalDateTime.now());
                List<UserInterestEntity> student5Interests = new ArrayList<>();
                UserInterestEntity s5Interest1 = new UserInterestEntity();
                s5Interest1.setUser(student5);
                s5Interest1.setInterest(interest8); // DevOps
                student5Interests.add(s5Interest1);
                UserInterestEntity s5Interest2 = new UserInterestEntity();
                s5Interest2.setUser(student5);
                s5Interest2.setInterest(interest6); // Microservicios
                student5Interests.add(s5Interest2);
                student5.setUserInterests(student5Interests);
                userRepository.save(student5);

                // Tutor 4
                UserEntity tutor4 = new UserEntity();
                tutor4.setFirstName("Marina");
                tutor4.setLastName("Suárez");
                tutor4.setEmail("marina.suarez@example.com");
                tutor4.setPassword(passwordEncoder.encode("password123"));
                tutor4.setBirthDate(LocalDate.of(1987, 6, 30));
                tutor4.setLocation("Tucumán");
                tutor4.setCurrentProfession("QA Automation Lead");
                tutor4.setCompany("QualitySoft");
                tutor4.setYearsOfExperience(9);
                tutor4.setProfessionalBio("Especialista en testing automatizado y calidad de software");
                tutor4.setHourlyRate(38.0);
                tutor4.setLinkedinUrl("https://linkedin.com/in/marina-suarez");
                tutor4.setBio("Me gusta ayudar a equipos a mejorar la calidad de sus productos");
                tutor4.setRole(Role.TUTOR);
                tutor4.setIsActive(true);
                tutor4.setIsVisible(true);
                tutor4.setCreatedAt(LocalDateTime.now());
                List<UserInterestEntity> tutor4Interests = new ArrayList<>();
                UserInterestEntity t4Interest1 = new UserInterestEntity();
                t4Interest1.setUser(tutor4);
                t4Interest1.setInterest(interest11); // Testing
                tutor4Interests.add(t4Interest1);
                UserInterestEntity t4Interest2 = new UserInterestEntity();
                t4Interest2.setUser(tutor4);
                t4Interest2.setInterest(interest7); // Gestión de Proyectos
                tutor4Interests.add(t4Interest2);
                tutor4.setUserInterests(tutor4Interests);
                userRepository.save(tutor4);

                // Tutor 5
                UserEntity tutor5 = new UserEntity();
                tutor5.setFirstName("Federico");
                tutor5.setLastName("Paz");
                tutor5.setEmail("federico.paz@example.com");
                tutor5.setPassword(passwordEncoder.encode("password123"));
                tutor5.setBirthDate(LocalDate.of(1990, 9, 18));
                tutor5.setLocation("Neuquén");
                tutor5.setCurrentProfession("Cloud Architect");
                tutor5.setCompany("Cloudify");
                tutor5.setYearsOfExperience(12);
                tutor5.setProfessionalBio("Arquitecto de soluciones cloud y microservicios");
                tutor5.setHourlyRate(55.0);
                tutor5.setLinkedinUrl("https://linkedin.com/in/federico-paz");
                tutor5.setBio("Disfruto diseñar infraestructuras escalables y seguras");
                tutor5.setRole(Role.TUTOR);
                tutor5.setIsActive(true);
                tutor5.setIsVisible(true);
                tutor5.setCreatedAt(LocalDateTime.now());
                List<UserInterestEntity> tutor5Interests = new ArrayList<>();
                UserInterestEntity t5Interest1 = new UserInterestEntity();
                t5Interest1.setUser(tutor5);
                t5Interest1.setInterest(interest10); // Cloud
                tutor5Interests.add(t5Interest1);
                UserInterestEntity t5Interest2 = new UserInterestEntity();
                t5Interest2.setUser(tutor5);
                t5Interest2.setInterest(interest6); // Microservicios
                tutor5Interests.add(t5Interest2);
                tutor5.setUserInterests(tutor5Interests);
                userRepository.save(tutor5);

                // Vistas de perfil adicionales
                UserViewedProfileEntity view5 = new UserViewedProfileEntity();
                view5.setViewer(student4);
                view5.setViewedUser(tutor4);
                view5.setStatus(Status.ACCEPTED);
                view5.setViewedAt(LocalDateTime.now().minusDays(2));
                userViewedProfileRepository.save(view5);

                UserViewedProfileEntity view6 = new UserViewedProfileEntity();
                view6.setViewer(student5);
                view6.setViewedUser(tutor5);
                view6.setStatus(Status.PENDING);
                view6.setViewedAt(LocalDateTime.now().minusDays(1));
                userViewedProfileRepository.save(view6);

                UserViewedProfileEntity view7 = new UserViewedProfileEntity();
                view7.setViewer(tutor4);
                view7.setViewedUser(student4);
                view7.setStatus(Status.ACCEPTED);
                view7.setViewedAt(LocalDateTime.now().minusDays(1));
                userViewedProfileRepository.save(view7);

                // Matches adicionales
                MatchEntity match5 = new MatchEntity();
                match5.setStudent(student4);
                match5.setTutor(tutor4);
                match5.setStatus(Status.ACCEPTED);
                match5.setCreatedAt(LocalDateTime.now().minusDays(2));
                matchRepository.save(match5);

                MatchEntity match6 = new MatchEntity();
                match6.setStudent(student5);
                match6.setTutor(tutor5);
                match6.setStatus(Status.PENDING);
                match6.setCreatedAt(LocalDateTime.now().minusDays(1));
                matchRepository.save(match6);

                MatchEntity match7 = new MatchEntity();
                match7.setStudent(student5);
                match7.setTutor(tutor4);
                match7.setStatus(Status.REJECTED);
                match7.setCreatedAt(LocalDateTime.now().minusDays(3));
                matchRepository.save(match7);

                // Notificaciones adicionales
                NotificationEntity notification5 = new NotificationEntity();
                notification5.setUserId(tutor4.getId());
                notification5.setMessage("Lucía Fernández te ha enviado una solicitud de conexión");
                notification5.setNotificationType(NotificationType.NEW_CONNECTION_REQUEST);
                notification5.setRelatedEntityId(match5.getId());
                notification5.setIsRead(false);
                notification5.setCreatedAt(LocalDateTime.now().minusDays(2));
                notificationRepository.save(notification5);

                NotificationEntity notification6 = new NotificationEntity();
                notification6.setUserId(student4.getId());
                notification6.setMessage("Marina Suárez ha aceptado tu solicitud de conexión");
                notification6.setNotificationType(NotificationType.CONNECTION_ACCEPTED);
                notification6.setRelatedEntityId(match5.getId());
                notification6.setIsRead(true);
                notification6.setCreatedAt(LocalDateTime.now().minusDays(1));
                notificationRepository.save(notification6);

                NotificationEntity notification7 = new NotificationEntity();
                notification7.setUserId(student5.getId());
                notification7.setMessage("Federico Paz ha recibido tu solicitud de conexión");
                notification7.setNotificationType(NotificationType.NEW_CONNECTION_REQUEST);
                notification7.setRelatedEntityId(match6.getId());
                notification7.setIsRead(false);
                notification7.setCreatedAt(LocalDateTime.now().minusDays(1));
                notificationRepository.save(notification7);

                NotificationEntity notification8 = new NotificationEntity();
                notification8.setUserId(student5.getId());
                notification8.setMessage("Marina Suárez ha rechazado tu solicitud de conexión");
                notification8.setNotificationType(NotificationType.CONNECTION_REJECTED);
                notification8.setRelatedEntityId(match7.getId());
                notification8.setIsRead(true);
                notification8.setCreatedAt(LocalDateTime.now().minusDays(2));
                notificationRepository.save(notification8);

                // Meetings adicionales
                MeetingEntity meeting2 = new MeetingEntity();
                meeting2.setDate(LocalDate.now().plusDays(1));
                meeting2.setDuration(2);
                meeting2.setHourlyRate(38.0);
                meeting2.setTime(LocalTime.of(15, 0));
                meeting2.setCreatedAt(LocalDateTime.now());
                meeting2.setMatch(match5);
                meeting2.setMentor(tutor4);
                meeting2.setStudent(student4);
                meeting2.setReason("Revisión de pruebas automatizadas");
                meeting2.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
                meetingRepository.save(meeting2);

                MeetingEntity meeting3 = new MeetingEntity();
                meeting3.setDate(LocalDate.now().plusDays(3));
                meeting3.setDuration(1);
                meeting3.setHourlyRate(55.0);
                meeting3.setTime(LocalTime.of(11, 0));
                meeting3.setCreatedAt(LocalDateTime.now());
                meeting3.setMatch(match6);
                meeting3.setMentor(tutor5);
                meeting3.setStudent(student5);
                meeting3.setReason("Consultoría sobre arquitectura cloud");
                meeting3.setStatus(MeetingEntity.MeetingStatus.PROPOSED);
                meetingRepository.save(meeting3);

                // ------------------ PAGOS ------------------
                PaymentEntity payment1 = new PaymentEntity();
                payment1.setMeeting(meeting2);
                payment1.setAmount(BigDecimal.valueOf(meeting2.getHourlyRate() * meeting2.getDuration()));
                payment1.setStatus(PaymentEntity.PaymentStatus.APPROVED);
                payment1.setDate(LocalDateTime.now());
                payment1.setPaymentMethod("MercadoPago");
                payment1.setTransactionId("MP-TRX-001");
                payment1.setMercadoPagoFee(payment1.getAmount().multiply(new BigDecimal("0.10"))); // 10% de comisión
                payment1.setPlatformFee(BigDecimal.valueOf(5.0));
                paymentRepository.save(payment1);

                PaymentEntity payment2 = new PaymentEntity();
                payment2.setMeeting(meeting);
                payment2.setAmount(BigDecimal.valueOf(meeting.getHourlyRate() * meeting.getDuration()));
                payment2.setStatus(PaymentEntity.PaymentStatus.PENDING);
                payment2.setDate(LocalDateTime.now().minusDays(1));
                payment2.setPaymentMethod("MercadoPago");
                payment2.setTransactionId("MP-TRX-002");
                payment2.setMercadoPagoFee(payment2.getAmount().multiply(new BigDecimal("0.10"))); // 10% de comisión
                payment2.setPlatformFee(BigDecimal.valueOf(5.0));
                paymentRepository.save(payment2);

                System.out.println("Datos iniciales cargados correctamente en todas las tablas");
            }
        };
    }
}