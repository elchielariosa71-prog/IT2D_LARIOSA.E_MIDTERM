package scc.dormitory.model;

public record ScenarioEntity(
        int id,
        String studentName,
        String building,
        String roomNo,
        double monthlyRent
) {
}
