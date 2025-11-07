package com.napier.devops;

import com.napier.devops.db.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseManagerTest {

    @Test
    void testGetConnection_success() throws SQLException {
        Connection mockConn = mock(Connection.class);

        try (MockedStatic<DriverManager> mocked = mockStatic(DriverManager.class)) {
            mocked.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            Connection conn = DatabaseManager.getConnection();
            assertNotNull(conn);
            assertEquals(mockConn, conn);
        }
    }

    @Test
    void testMultipleConnections() throws SQLException {
        Connection mockConn1 = mock(Connection.class);
        Connection mockConn2 = mock(Connection.class);

        try (MockedStatic<DriverManager> mocked = mockStatic(DriverManager.class)) {
            mocked.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn1, mockConn2);

            Connection conn1 = DatabaseManager.getConnection();
            Connection conn2 = DatabaseManager.getConnection();

            assertNotNull(conn1);
            assertNotNull(conn2);
            assertNotSame(conn1, conn2, "Each call should return a new connection");
        }
    }

    @Test
    void testConnection_closesProperly() throws SQLException {
        Connection mockConn = mock(Connection.class);

        try (MockedStatic<DriverManager> mocked = mockStatic(DriverManager.class)) {
            mocked.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);

            Connection conn = DatabaseManager.getConnection();
            conn.close();

            verify(mockConn).close();
        }
    }

}
