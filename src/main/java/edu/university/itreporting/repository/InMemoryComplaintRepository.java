package edu.university.itreporting.repository;

import edu.university.itreporting.model.Complaint;
import edu.university.itreporting.model.Priority;
import edu.university.itreporting.model.Status;
import edu.university.itreporting.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class InMemoryComplaintRepository implements ComplaintRepository {
    private final Map<Integer, Complaint> complaintsByMemoId = new LinkedHashMap<>();

    @Override
    public Complaint save(Complaint complaint) {
        if (complaint == null) {
            throw new IllegalArgumentException("complaint is required.");
        }
        complaintsByMemoId.put(complaint.getMemoId(), complaint);
        return complaint;
    }

    @Override
    public Optional<Complaint> findByMemoId(int memoId) {
        return Optional.ofNullable(complaintsByMemoId.get(memoId));
    }

    @Override
    public List<Complaint> findAll() {
        return new ArrayList<>(complaintsByMemoId.values());
    }

    @Override
    public List<Complaint> findByOwner(User owner) {
        List<Complaint> results = new ArrayList<>();
        if (owner == null) {
            return results;
        }
        for (Complaint complaint : complaintsByMemoId.values()) {
            if (owner.equals(complaint.getOwner())) {
                results.add(complaint);
            }
        }
        return results;
    }

    @Override
    public List<Complaint> findByStatus(Status status) {
        List<Complaint> results = new ArrayList<>();
        for (Complaint complaint : complaintsByMemoId.values()) {
            if (complaint.getStatus() == status) {
                results.add(complaint);
            }
        }
        return results;
    }

    @Override
    public List<Complaint> findByPriority(Priority priority) {
        List<Complaint> results = new ArrayList<>();
        for (Complaint complaint : complaintsByMemoId.values()) {
            if (complaint.getPriority() == priority) {
                results.add(complaint);
            }
        }
        return results;
    }

    @Override
    public List<Complaint> findByLocation(String location) {
        List<Complaint> results = new ArrayList<>();
        if (location == null) {
            return results;
        }
        String normalized = location.trim().toLowerCase(Locale.ROOT);
        for (Complaint complaint : complaintsByMemoId.values()) {
            if (complaint.getLocation().trim().toLowerCase(Locale.ROOT).equals(normalized)) {
                results.add(complaint);
            }
        }
        return results;
    }

    @Override
    public List<Complaint> findByEquipmentType(String equipmentType) {
        List<Complaint> results = new ArrayList<>();
        if (equipmentType == null) {
            return results;
        }
        String normalized = equipmentType.trim().toLowerCase(Locale.ROOT);
        for (Complaint complaint : complaintsByMemoId.values()) {
            if (complaint.getEquipmentType().trim().toLowerCase(Locale.ROOT).equals(normalized)) {
                results.add(complaint);
            }
        }
        return results;
    }

    @Override
    public List<Complaint> filter(String location, String equipmentType, Status status, Priority priority) {
        List<Complaint> results = new ArrayList<>();
        String normalizedLocation = location == null ? null : location.trim().toLowerCase(Locale.ROOT);
        String normalizedType = equipmentType == null ? null : equipmentType.trim().toLowerCase(Locale.ROOT);
        for (Complaint complaint : complaintsByMemoId.values()) {
            if (normalizedLocation != null
                    && !complaint.getLocation().trim().toLowerCase(Locale.ROOT).equals(normalizedLocation)) {
                continue;
            }
            if (normalizedType != null
                    && !complaint.getEquipmentType().trim().toLowerCase(Locale.ROOT).equals(normalizedType)) {
                continue;
            }
            if (status != null && complaint.getStatus() != status) {
                continue;
            }
            if (priority != null && complaint.getPriority() != priority) {
                continue;
            }
            results.add(complaint);
        }
        return results;
    }

    @Override
    public boolean deleteByMemoId(int memoId) {
        return complaintsByMemoId.remove(memoId) != null;
    }
}
