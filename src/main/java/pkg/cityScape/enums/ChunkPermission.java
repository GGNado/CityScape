package pkg.cityScape.enums;

public enum ChunkPermission {

    // --- Eventi di Costruzione e Distruzione ---
    BUILD,           // Possono piazzare blocchi (BlockPlaceEvent)
    BREAK,           // Possono rompere blocchi (BlockBreakEvent)

    // --- Eventi di Interazione con Contenitori e Macchinari ---
    OPEN_CHEST,      // Possono aprire bauli (PlayerInteractEvent su bauli)
    USE_FURNACE,     // Possono usare fornaci (PlayerInteractEvent su fornaci)
    USE_CRAFTING_TABLE, // Possono usare tavoli da lavoro (PlayerInteractEvent su crafting table)

    // --- Eventi di Combattimento ---
    ATTACK_ANIMALS,  // Possono attaccare animali (EntityDamageByEntityEvent su animali)
    ATTACK_MONSTERS, // Possono attaccare mostri (EntityDamageByEntityEvent su mostri)
    PVP,             // Possono partecipare al PvP (EntityDamageByEntityEvent su altri giocatori)

    // --- Eventi di Interazione con Porte e Bottoni ---
    INTERACT_DOOR,   // Possono usare porte o botole (PlayerInteractEvent su porte o botole)
    INTERACT_BUTTON, // Possono usare pulsanti o leve (PlayerInteractEvent su pulsanti o leve)
    INTERACT_PRESSURE_PLATE, // Possono usare le pressure plate (PlayerInteractEvent su pressure plate)

    // --- Eventi di Interazione con Ambienti ---
    FARM,            // Possono coltivare o raccogliere raccolti (PlayerInteractEvent su blocchi agricoli)
    RIDE_HORSE       // Possono cavalcare cavalli (EntityMountEvent)
}