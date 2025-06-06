package com.scaffold.template.entities;

public enum Status {
    PENDING,    // El tutor fue mostrado pero no hay decisión
    REJECTED,   // El estudiante descartó explícitamente al tutor
    ACCEPTED,    // El estudiante mostró interés (falta confirmación del tutor)
    REJECTED_BY_TUTOR, // El tutor rechazó al estudiante
}
