package DAO;

import java.sql.*;

public class StatisticsDAO {

    public static class TransactionStats {//klasa wewnetrzna do przechowania statystyki transakcji
        public int totalCount; //liczba wszystkich transakcji itd.
        public double totalSum;
        public double average;

        public TransactionStats(int totalCount, double totalSum, double average) {
            this.totalCount = totalCount;
            this.totalSum = totalSum;
            this.average = average;
        }
    }

    public TransactionStats getTransactionStatistics() throws SQLException {
        String sql = "SELECT COUNT(*) as total_count, SUM(calkowita_kwota) as total_sum FROM transakcje";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int totalCount = rs.getInt("total_count");
                double totalSum = rs.getDouble("total_sum");
                double average = totalCount > 0 ? totalSum / totalCount : 0;
                return new TransactionStats(totalCount, totalSum, average);
            }
            return new TransactionStats(0, 0, 0); // domyslne wartosci
        }
    }

    public TransactionStats getFilteredTransactionStatistics(String transactionType, Date fromDate, Date toDate) throws SQLException {// Metoda do pobierania statystyk transakcji z filtrami
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) as total_count, SUM(calkowita_kwota) as total_sum FROM transakcje WHERE 1=1"
        );
        //dynamicznie budujemy zapytanie na podstawie dostarczonych parametrow
        if (transactionType != null && !transactionType.isEmpty()) {
            sql.append(" AND typ = '").append(transactionType).append("'");//typ transakcji
        }

        if (fromDate != null) {
            sql.append(" AND data >= '").append(new java.sql.Timestamp(fromDate.getTime())).append("'");//data poczatkowa
        }

        if (toDate != null) {
            sql.append(" AND data <= '").append(new java.sql.Timestamp(toDate.getTime())).append("'");//data koncowa
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int totalCount = rs.getInt("total_count");
                double totalSum = rs.getDouble("total_sum");
                double average = totalCount > 0 ? totalSum / totalCount : 0;
                return new TransactionStats(totalCount, totalSum, average);
            }
            return new TransactionStats(0, 0, 0);
        }
    }
}