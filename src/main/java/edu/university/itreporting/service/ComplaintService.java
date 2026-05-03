package edu.university.itreporting.service;

import edu.university.itreporting.model.Complaint;
import edu.university.itreporting.model.Faculty;
import edu.university.itreporting.model.ITStaff;
import edu.university.itreporting.model.Priority;
import edu.university.itreporting.model.ResolutionNote;
import edu.university.itreporting.model.Status;
import edu.university.itreporting.model.Student;
import edu.university.itreporting.model.User;
import edu.university.itreporting.model.UserRole;
import edu.university.itreporting.repository.ComplaintRepository;
import edu.university.itreporting.repository.UserRepository;
import edu.university.itreporting.util.IdSequence;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final IdSequence memoIdSequence;
    private final IdSequence noteIdSequence;

    public ComplaintService(
            ComplaintRepository complaintRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.memoIdSequence = new IdSequence(nextMemoIdSeed(complaintRepository.findAll()));
        this.noteIdSequence = new IdSequence(nextNoteIdSeed(complaintRepository.findAll()));
    }

    public Complaint submitComplaint(
            User submitter,
            String location,
            String equipmentType,
            String equipmentId,
            String description
    ) {
        requireComplaintSubmitter(submitter);

        Complaint complaint = new Complaint(
                memoIdSequence.next(),
                description,
                location,
                equipmentType,
                equipmentId,
                LocalDate.now(),
                submitter
        );
        complaintRepository.save(complaint);
        if (submitter instanceof Student student) {
            student.addComplaint(complaint);
        } else if (submitter instanceof Faculty faculty) {
            faculty.addComplaint(complaint);
        }
        notificationService.notifyUser(submitter, "Memo #" + complaint.getMemoId() + " submitted successfully.");
        return complaint;
    }

    public List<Complaint> viewSubmittedComplaints(User submitter) {
        requireComplaintSubmitter(submitter);
        return complaintRepository.findByOwner(submitter);
    }

    public Complaint trackComplaintStatus(User submitter, int memoId) {
        requireComplaintSubmitter(submitter);
        Complaint complaint = findComplaintOrThrow(memoId);
        if (!submitter.equals(complaint.getOwner())) {
            throw new IllegalArgumentException("You can only track your own complaints.");
        }
        return complaint;
    }

    public List<Complaint> viewAllComplaints(ITStaff actor) {
        requireRole(actor, UserRole.IT_STAFF, "Only IT Staff can view all complaints.");
        return complaintRepository.findAll();
    }

    public List<Complaint> filterComplaints(
            ITStaff actor,
            String location,
            String equipmentType,
            Status status,
            Priority priority
    ) {
        requireRole(actor, UserRole.IT_STAFF, "Only IT Staff can filter complaints.");
        return complaintRepository.filter(location, equipmentType, status, priority);
    }

    public Complaint assignPriority(ITStaff actor, int memoId, Priority priority) {
        requireRole(actor, UserRole.IT_STAFF, "Only IT Staff can assign priority.");
        Complaint complaint = findComplaintOrThrow(memoId);
        complaint.assignStaff(actor);
        complaint.setPriority(priority);
        complaintRepository.save(complaint);
        return complaint;
    }

    public Complaint updateStatus(ITStaff actor, int memoId, Status status) {
        requireRole(actor, UserRole.IT_STAFF, "Only IT Staff can update status.");
        Complaint complaint = findComplaintOrThrow(memoId);
        complaint.assignStaff(actor);
        complaint.updateStatus(status);
        complaintRepository.save(complaint);
        notificationService.notifyUser(
                complaint.getOwner(),
                "Memo #" + complaint.getMemoId() + " status updated to " + status.getDisplayName() + "."
        );
        return complaint;
    }

    public ResolutionNote addResolutionNote(ITStaff actor, int memoId, String text) {
        requireRole(actor, UserRole.IT_STAFF, "Only IT Staff can add resolution notes.");
        Complaint complaint = findComplaintOrThrow(memoId);
        complaint.assignStaff(actor);
        ResolutionNote note = new ResolutionNote(noteIdSequence.next(), text, LocalDate.now(), actor);
        complaint.addNote(note);
        complaintRepository.save(complaint);
        return note;
    }

    public Complaint findComplaintByMemoId(int memoId) {
        return findComplaintOrThrow(memoId);
    }

    private Complaint findComplaintOrThrow(int memoId) {
        return complaintRepository.findByMemoId(memoId)
                .orElseThrow(() -> new NoSuchElementException("Memo #" + memoId + " not found."));
    }

    private void requireComplaintSubmitter(User user) {
        if (user == null || (user.getRole() != UserRole.STUDENT && user.getRole() != UserRole.FACULTY)) {
            throw new IllegalArgumentException("Only Student/Faculty users can submit or track complaints.");
        }
    }

    private static void requireRole(User actor, UserRole expectedRole, String message) {
        if (actor == null || actor.getRole() != expectedRole) {
            throw new IllegalArgumentException(message);
        }
    }

    private static int nextMemoIdSeed(List<Complaint> complaints) {
        return complaints.stream()
                .mapToInt(Complaint::getMemoId)
                .max()
                .orElse(1000) + 1;
    }

    private static int nextNoteIdSeed(List<Complaint> complaints) {
        return complaints.stream()
                .filter(Objects::nonNull)
                .flatMap(complaint -> complaint.getNotes().stream())
                .mapToInt(ResolutionNote::getNoteId)
                .max()
                .orElse(0) + 1;
    }
}
