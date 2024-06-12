package controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class UserProgramTest {

    UserProgram userProgram;
    @BeforeEach
    void setUp() {
    userProgram = new UserProgram();
    }

    @AfterEach
    void tearDown() {
        userProgram = null;
    }
}