package edu.university.itreporting.repository;

import edu.university.itreporting.model.Complaint;
import edu.university.itreporting.model.Priority;
import edu.university.itreporting.model.Status;
import edu.university.itreporting.model.User;

import java.util.List;
import java.util.Optional;

public interface ComplaintRepository {
    Complaint save(Complaint complaint);

    Optional<Complaint> findByMemoId(int memoId);

    List<Complaint> findAll();

    List<Complaint> findByOwner(User owner);

    List<Complaint> findByStatus(Status status);

    List<Complaint> findByPriority(Priority priority);

    List<Complaint> findByLocation(String location);

    List<Complaint> findByEquipmentType(String equipmentType);

    List<Complaint> filter(String location, String equipmentType, Status status, Priority priority);

    boolean deleteByMemoId(int memoId);
}
