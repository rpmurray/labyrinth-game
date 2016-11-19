package info.masterfrog.labyrinth.entity.builder;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.enumeration.ResourceHandle;
import info.masterfrog.labyrinth.enumeration.SpriteSheetHandle;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.resource.ImageResource;
import info.masterfrog.pixelcat.engine.logic.resource.ResourceFactory;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteResource;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteSheet;

import java.util.HashMap;
import java.util.Map;

public class LabyrinthRoomResourceManager {
    private Map<SpriteSheetHandle, SpriteSheet> spriteSheets;

    private static LabyrinthRoomResourceManager instance;
    private static EnvironmentManager environmentManager = EnvironmentManager.getInstance();
    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();

    public static LabyrinthRoomResourceManager getInstance() {
        if (instance == null) {
            instance = new LabyrinthRoomResourceManager();
        }

        return instance;
    }

    private LabyrinthRoomResourceManager() {
        // setup
        this.spriteSheets = new HashMap<>();
    }

    public ImageResource getImageResource(ResourceHandle resourceHandle) throws TransientGameException{
        // setup
        ImageResource resource;

        // generation + lookup
        switch (resourceHandle) {
            case LABYRINTH__IMAGE__ROOM_BACKGROUND_BASE:
                if (!environmentManager.hasResource(ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_BASE)) {
                    environmentManager.registerResource(
                        ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_BASE,
                        resourceFactory.createImageResource(
                            getSpriteResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_BASE)
                        )
                    );
                }

                resource = (ImageResource) environmentManager.getResource(ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_BASE);
                break;
            case LABYRINTH__IMAGE__ROOM_BACKGROUND_1X1:
                if (!environmentManager.hasResource(ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_1X1)) {
                    environmentManager.registerResource(
                        ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_1X1,
                        resourceFactory.createImageResource(
                            getSpriteResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_1X1)
                        )
                    );
                }

                resource = (ImageResource) environmentManager.getResource(ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_1X1);
                break;
            case LABYRINTH__IMAGE__ROOM_BACKGROUND_3X3:
                if (!environmentManager.hasResource(ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_3X3)) {
                    environmentManager.registerResource(
                        ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_3X3,
                        resourceFactory.createImageResource(
                            getSpriteResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_3X3)
                        )
                    );
                }

                resource = (ImageResource) environmentManager.getResource(ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_3X3);
                break;
            default:
                throw new TransientGameException(LabyrinthGameErrorCode.LEVEL__LABYRINTH__RESOURCE_REFERENCE_ERROR);
        }

        return resource;
    }

    public SpriteResource getSpriteResource(ResourceHandle resourceHandle) throws TransientGameException {
        // setup
        SpriteResource resource;

        // generation + lookup
        switch (resourceHandle) {
            case LABYRINTH__SPRITE__ROOM_BACKGROUND_BASE:
                if (!environmentManager.hasResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_BASE)) {
                    environmentManager.registerResource(
                        ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_BASE,
                        resourceFactory.createSpriteResource(
                            0, 0, getSpriteSheet(SpriteSheetHandle.LABYRINTH__SPRITE_SHEET__ROOM_BACKGROUND)
                        )
                    );
                }

                resource = (SpriteResource) environmentManager.getResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_BASE);
                break;
            case LABYRINTH__SPRITE__ROOM_BACKGROUND_1X1:
                if (!environmentManager.hasResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_1X1)) {
                    environmentManager.registerResource(
                        ResourceHandle.LABYRINTH__IMAGE__ROOM_BACKGROUND_1X1,
                        resourceFactory.createSpriteResource(
                            1, 0, getSpriteSheet(SpriteSheetHandle.LABYRINTH__SPRITE_SHEET__ROOM_BACKGROUND)
                        )
                    );
                }

                resource = (SpriteResource) environmentManager.getResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_1X1);
                break;
            case LABYRINTH__SPRITE__ROOM_BACKGROUND_3X3:
                if (!environmentManager.hasResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_3X3)) {
                    environmentManager.registerResource(
                        ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_3X3,
                        resourceFactory.createSpriteResource(
                            2, 0, getSpriteSheet(SpriteSheetHandle.LABYRINTH__SPRITE_SHEET__ROOM_BACKGROUND)
                        )
                    );
                }

                resource = (SpriteResource) environmentManager.getResource(ResourceHandle.LABYRINTH__SPRITE__ROOM_BACKGROUND_3X3);
                break;
            default:
                throw new TransientGameException(LabyrinthGameErrorCode.LEVEL__LABYRINTH__RESOURCE_REFERENCE_ERROR);
        }

        return resource;
    }

    public SpriteSheet getSpriteSheet(SpriteSheetHandle spriteSheetHandle) throws TransientGameException {
        // setup
        SpriteSheet spriteSheet;

        // generation + lookup
        switch (spriteSheetHandle) {
            case LABYRINTH__SPRITE_SHEET__ROOM_BACKGROUND:
                // generate as needed
                if (!spriteSheets.containsKey(SpriteSheetHandle.LABYRINTH__SPRITE_SHEET__ROOM_BACKGROUND)) {
                    spriteSheets.put(
                        SpriteSheetHandle.LABYRINTH__SPRITE_SHEET__ROOM_BACKGROUND,
                        resourceFactory.createSpriteSheet(
                            "room-sprite-sheet.png",
                            256, 256
                        )
                    );
                }

                // lookup
                spriteSheet = spriteSheets.get(SpriteSheetHandle.LABYRINTH__SPRITE_SHEET__ROOM_BACKGROUND);
                break;
            default:
                throw new TransientGameException(LabyrinthGameErrorCode.LEVEL__LABYRINTH__RESOURCE_REFERENCE_ERROR);
        }

        return spriteSheet;
    }
}
