package edu.university.itreporting.util;

import edu.university.itreporting.model.Admin;
import edu.university.itreporting.model.Complaint;
import edu.university.itreporting.model.Faculty;
import edu.university.itreporting.model.ITStaff;
import edu.university.itreporting.model.Priority;
import edu.university.itreporting.model.Report;
import edu.university.itreporting.model.ResolutionNote;
import edu.university.itreporting.model.Status;
import edu.university.itreporting.model.Student;
import edu.university.itreporting.repository.ComplaintRepository;
import edu.university.itreporting.repository.InMemoryComplaintRepository;
import edu.university.itreporting.repository.InMemoryReportRepository;
import edu.university.itreporting.repository.InMemoryUserRepository;
import edu.university.itreporting.repository.ReportRepository;
import edu.university.itreporting.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

public final class SeedDataInitializer {
    private SeedDataInitializer() {
    }

    public static SeedDataBundle initializeDefaultData() {
        UserRepository userRepository = new InMemoryUserRepository();
        ComplaintRepository complaintRepository = new InMemoryComplaintRepository();
        ReportRepository reportRepository = new InMemoryReportRepository();

        IdSequence userIdSeq = new IdSequence(1);
        IdSequence complaintIdSeq = new IdSequence(1001);
        IdSequence noteIdSeq = new IdSequence(1);
        IdSequence reportIdSeq = new IdSequence(5001);

        Admin admin = new Admin(userIdSeq.next(), 1, "System Admin", "admin@university.edu", "admin123");
        ITStaff staffAli = new ITStaff(userIdSeq.next(), 2001, "Ali Raza", "ali.it@university.edu", "it123");
        ITStaff staffFatima = new ITStaff(userIdSeq.next(), 2002, "Fatima Khan", "fatima.it@university.edu", "it123");
        Student student = new Student(userIdSeq.next(), "Ahmed Student", "ahmed.student@university.edu", "user123");
        Faculty faculty = new Faculty(userIdSeq.next(), "Dr. Sana", "sana.faculty@university.edu", "user123");

        for (var user : List.of(admin, staffAli, staffFatima, student, faculty)) {
            userRepository.save(user);
        }

        Complaint complaint1 = new Complaint(
                complaintIdSeq.next(),
                "Projector in CS-102 is not turning on.",
                "CS-102",
                "Projector",
                "PRJ-CS102-01",
                LocalDate.now().minusDays(2),
                faculty
        );
        complaint1.setPriority(Priority.HIGH);
        complaint1.assignStaff(staffAli);
        complaint1.updateStatus(Status.IN_PROGRESS);
        complaint1.addNote(new ResolutionNote(
                noteIdSeq.next(),
                "Checked power cable and replaced faulty adapter.",
                LocalDate.now().minusDays(1),
                staffAli
        ));
        faculty.addComplaint(complaint1);
        complaintRepository.save(complaint1);

        Complaint complaint2 = new Complaint(
                complaintIdSeq.next(),
                "Mouse not working in Programming Lab PC-17.",
                "Programming Lab A",
                "Mouse",
                "PC-17",
                LocalDate.now().minusDays(1),
                student
        );
        complaint2.setPriority(Priority.MEDIUM);
        complaint2.assignStaff(staffFatima);
        complaint2.updateStatus(Status.RESOLVED);
        complaint2.addNote(new ResolutionNote(
                noteIdSeq.next(),
                "Replaced damaged USB mouse and verified input.",
                LocalDate.now(),
                staffFatima
        ));
        student.addComplaint(complaint2);
        complaintRepository.save(complaint2);

        Complaint complaint3 = new Complaint(
                complaintIdSeq.next(),
                "Network cable disconnected in Lab B row 2.",
                "Programming Lab B",
                "Network Cable",
                "NC-LB-R2-08",
                LocalDate.now(),
                student
        );
        complaint3.setPriority(Priority.LOW);
        complaint3.updateStatus(Status.PENDING);
        student.addComplaint(complaint3);
        complaintRepository.save(complaint3);

        Report report = new Report(reportIdSeq.next(), LocalDate.now(), complaintRepository.findAll());
        admin.addReport(report);
        reportRepository.save(report);

        return new SeedDataBundle(userRepository, complaintRepository, reportRepository);
    }
}
