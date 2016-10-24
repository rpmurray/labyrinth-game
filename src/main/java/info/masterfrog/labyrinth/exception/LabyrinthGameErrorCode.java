package info.masterfrog.labyrinth.exception;

import info.masterfrog.pixelcat.engine.exception.GameErrorCode;

public enum LabyrinthGameErrorCode implements GameErrorCode {
    /* Error codes below 100,000 are reserved for use by the game engine, higher error codes are free for game implementation use */

    // 11XXXXs codes reserved for environment related errors

    // 110XXXs codes reserved for general environment related errors
    // N/A

    // 111XXXs codes reserved for environment configuration errors
    // N/A

    // 112XXXs codes reserved for environment management errors
    ENVIRONMENT__MANAGEMENT__UNDEFINED_RESOURCE_ACCESS(112001L, "Undefined resource access attempt made and failed."),
    ENVIRONMENT__MANAGEMENT__UNDEFINED_ENTITY_ACCESS(112002L, "Undefined entity access attempt made and failed."),
    ENVIRONMENT__MANAGEMENT__UNDEFINED_ENTITY_MANAGER_ACCESS(112003L, "Undefined entity manager access attempt made and failed."),
    ENVIRONMENT__MANAGEMENT__UNDEFINED_LEVEL_ENTITIES_ACCESS(112004L, "Undefined level entities access attempt made and failed."),

    // 113XXXs codes reserved for entities & entity builder errors
    // N/A


    // 12XXXXs codes reserved for level related logic errors

    // 120XXXs codes reserved for general level handling logic errors
    LEVEL__CORE__UNDEFINED_LEVEL(120001L, "Undefined level referenced."),

    // 121XXXs codes reserved for start screen logic errors
    LEVEL__START_SCREEN__INVALID_STATE(121001L, "Invalid start screen state referenced."),
    LEVEL__LABYRINTH__INVALID_STATE(121002L, "Invalid labyrinth level state referenced."),
    ;

    private Long code;
    private String message;

    LabyrinthGameErrorCode(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GameEngineErrorCode{" +
            "code=" + code +
            ", message='" + message + '\'' +
            '}';
    }
}
