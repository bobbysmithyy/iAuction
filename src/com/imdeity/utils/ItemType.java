package com.imdeity.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * ItemType types.
 *
 * @author sk89q, updated by vvenaya
 */
public enum ItemType {
    // Blocks
    AIR(0, "Air"),
    STONE(1, "Stone",  "rock"),
    GRASS(2, "Grass"),
    DIRT(3, "Dirt"),
    COBBLESTONE(4, "Cobblestone", "cobble"),
    WOOD(5, "Wood", new String[] {"woodplank", "plank", "woodplanks", "planks"}),
    SAPLING(6,0, "Sapling"),
    SPRUCE_SAPLING(6,1, "Spruce Sapling", "spruce"),
    BIRCH_SAPLING(6,2, "Birch Sapling", "birch"),
    BEDROCK(7, "Bedrock", "adminium"),
    WATER(8, "Water", new String[] {"watermoving", "movingwater"}),
    STATIONARY_WATER(9, "Water (stationary)",
             new String[] {"water", "waterstationary", "stationarywater", "stillwater"}),
    LAVA(10, "Lava", new String[] {"lavamoving", "movinglava"}),
    STATIONARY_LAVA(11, "Lava (stationary)",
             new String[] { "lavastationary", "stationarylava", "stilllava","lava"}),
    SAND(12, "Sand"),
    GRAVEL(13, "Gravel"),
    GOLD_ORE(14, "Gold ore"),
    IRON_ORE(15, "Iron ore"),
    COAL_ORE(16, "Coal ore"),
    LOG(17, 0, "Log",  "tree"),
    SPRUCE_LOG(17, 1, "Spruce Log"),
    BIRCH_LOG(17, 2, "Birch Log"),
    LEAVES(18,0,  "Leaves", "leaf"),
    SPRUCE_LEAVES(18,1, "Spruce Leaves", "spruceleaf"),
    BIRCH_LEAVES(18,2, "Birch Leaves",  "birchleaf"),
    SPONGE(19, "Sponge"),
    GLASS(20, "Glass"),
    LAPIS_LAZULI_ORE(21, "Lapis lazuli ore", new String[] { "blueore", "lapisore"}),
    LAPIS_LAZULI_BLOCK(22, "Lapis lazuli block", new String[] {"bluerock","lapisblock"}),
    DISPENSER(23, "Dispenser"),
    SANDSTONE(24, "Sandstone"),
    NOTE_BLOCK(25, "Note block", new String[] {"musicblock", "note", "music", "instrument"}),
    BED(26, "Bed"),
    POWERED_RAIL(27, "Powered Rail",
            new String[] {"boosterrail", "poweredtrack", "boostertrack"}),
    DETECTOR_RAIL(28, "Detector Rail"),
    STICKY_PISTON(29, "Sticky Piston"),
    WEB(30, "Web", "spiderweb"),
    TALL_GRASS_DEADSHRUB(31,0,"Dead shrub","deadschrub"),
    TALL_GRASS(31,1,"Tall Grass","tallgrass"),
    TALL_GRASS_FERN(31,2,"Fern"),
    TALL_GRASS_DEADSHRUB2(32,"Dead shrub 2"),
    PISTON(33,"Piston"),
    PISTON_EXTENTION(34,"Piston extention"),
    WHITE_WOOL(35,0,"White wool", new String[] {"whitecloth","white","cloth","wool"},ChatColor.WHITE), 
    ORANGE_WOOL(35,1,"Orange wool", new String[] {"orangecloth","orange"},ChatColor.GOLD),
    MAGENTA_WOOL(35,2,"Magenta wool", new String[] {"magentacloth","magenta"}),
    LIGHTBLUE_WOOL(35,3,"Light Blue wool", new String[] {"lightbluecloth","lightblue"},ChatColor.BLUE),
    YELLOW_WOOL(35,4,"Yellow wool", new String[] {"yellowcloth","yellow"},ChatColor.YELLOW),
    LIGHTGREEN_WOOL(35,5,"Light Green wool", new String[] {"lightgreencloth","lightgreen"},ChatColor.GREEN),
    PINK_WOOL(35,6,"Pink wool", new String[] {"pinkcloth","pink"}),
    GRAY_WOOL(35,7,"Gray wool", new String[] {"graycloth","gray"},ChatColor.GRAY),
    LIGHTGRAY_WOOL(35,8,"LightGray wool", new String[] {"lightgraycloth","lightgray"},ChatColor.GRAY),
    CYAN_WOOL(35,9,"Cyan wool", new String[] {"cyancloth","cyan"}),
    PURPLE_WOOL(35,10,"Purple wool", new String[] {"purplecloth","purple"},ChatColor.DARK_PURPLE),
    BLUE_WOOL(35,11,"Blue wool", new String[] {"bluecloth","blue"},ChatColor.BLUE),
    BROWN_WOOL(35,12,"Brown wool", new String[] {"browncloth","brown"},ChatColor.GOLD),
    DARKGREEN_WOOL(35,13,"Dark Green wool", new String[] {"darkgreencloth","darkgreen"},ChatColor.DARK_GREEN),
    RED_WOOL(35,14,"Red wool", new String[] {"redcloth","red"},ChatColor.RED),
    BLACK_WOOL(35,15,"Black wool", new String[] {"blackcloth","black"},ChatColor.BLACK),
    PISTON_BLOCK(36,"Piston block"),
    YELLOW_FLOWER(37, "Yellow flower",  "flower",ChatColor.YELLOW),
    RED_FLOWER(38, "Red rose", new String[] {"redflower", "rose"},ChatColor.RED),
    BROWN_MUSHROOM(39, "Brown mushroom", "mushroom"),
    RED_MUSHROOM(40, "Red mushroom"),
    GOLD_BLOCK(41, "Gold block", "gold"),
    IRON_BLOCK(42, "Iron block", "iron"),
    DOUBLE_STEP(43, "Double step", new String[] {"doubleslab", "doublestoneslab"}),
    SANDSTONE_DOUBLE_STEP(43, 1,  "Sandstone Double step", new String[] {"sandstonedoubleslab", "sandstonedoublestoneslab"}),
    WOODEN_DOUBLE_STEP(43, 2, "Wooden Double step", new String[] {"woodendoubleslab", "woodendoublestoneslab"}),
    COBBLESTONE_DOUBLE_STEP(43, 3, "Cobblestone Double step", new String[] {"cobblestonedoubleslab", "cobblestonedoublestoneslab"}),

    STEP(44, "Step", new String[] {"slab", "stoneslab", "halfstep"}),
    SANDSTONE_STEP(44, 1,  "Sandstone step", new String[] {"sandstoneslab"}),
    WOODEN_STEP(44, 2, "Wooden step", "woodenslab"),
    COBBLESTONE_STEP(44, 3, "Cobblestone step", "cobblestoneslab"),
    BRICK(45, "Brick", "brickblock"),
    TNT(46, "TNT"),
    BOOKCASE(47, "Bookcase", new String[] {"bookshelf", "bookshelves"}),
    MOSSY_COBBLESTONE(48, "Cobblestone (mossy)",
            new String[] {"mossycobblestone", "mossstone", "mossystone",
            "mosscobble", "mossycobble", "moss", "mossy", "sossymobblecone"}),
    OBSIDIAN(49, "Obsidian"),
    TORCH(50, "Torch"),
    FIRE(51, "Fire", new String[] {"flame", "flames"}),
    MOB_SPAWNER(52, "Mob spawner", "spawner"),
    WOODEN_STAIRS(53, "Wooden stairs",
            new String[] {"woodstair", "woodstairs", "woodenstair"}),
    CHEST(54, "Chest",  "storage"),
    REDSTONE_WIRE(55, "Redstone wire", "redstone"),
    DIAMOND_ORE(56, "Diamond ore"),
    DIAMOND_BLOCK(57, "Diamond block", new String[] {"diamond", "diamondblock"}),
    WORKBENCH(58, "Workbench", new String[] {"workbench", "table", "craftingtable"}),
    CROPS(59, "Crops", new String[] {"crops", "crop", "plant", "plants"}),
    SOIL(60, "Soil", new String[] {"soil", "farmland"}),
    FURNACE(61, "Furnace", "furnace"),
    BURNING_FURNACE(62, "Furnace (burning)", new String[] {"burningfurnace", "litfurnace"}),
    SIGN_POST(63, "Sign post", new String[] {"sign", "signpost"}),
    WOODEN_DOOR(64, "Wooden door", new String[] {"wooddoor", "woodendoor", "door"}),
    LADDER(65, "Ladder", "ladder"),
    MINECART_TRACKS(66, "Minecart tracks",
            new String[] {"track", "tracks", "minecrattrack", "minecarttracks", "rails", "rail"}),
    COBBLESTONE_STAIRS(67, "Cobblestone stairs",
            new String[] {"cobblestonestair", "cobblestonestairs", "cobblestair", "cobblestairs"}),
    WALL_SIGN(68, "Wall sign", "wallsign"),
    LEVER(69, "Lever", new String[] {"lever", "switch", "stonelever", "stoneswitch"}),
    STONE_PRESSURE_PLATE(70, "Stone pressure plate",
            new String[] {"stonepressureplate", "stoneplate"}),
    IRON_DOOR(71, "Iron Door", "irondoor"),
    WOODEN_PRESSURE_PLATE(72, "Wooden pressure plate",
            new String[] {"woodpressureplate", "woodplate", "woodenpressureplate", "woodenplate"}),
    REDSTONE_ORE(73, "Redstone ore", "redstoneore"),
    GLOWING_REDSTONE_ORE(74, "Glowing redstone ore", "glowingredstoneore"),
    REDSTONE_TORCH_OFF(75, "Redstone torch (off)",
            new String[] {"redstonetorchoff", "rstorchoff"}),
    REDSTONE_TORCH_ON(76, "Redstone torch (on)",
            new String [] {"redstonetorch", "redstonetorchon", "rstorchon"}),
    STONE_BUTTON(77, "Stone Button","button"),
    SNOW(78, "Snow", "snow"),
    ICE(79, "Ice", "ice"),
    SNOW_BLOCK(80, "Snow block"),
    CACTUS(81, "Cactus",  "cacti"),
    CLAY(82, "Clay"),
    SUGAR_CANE(83, "Reed", new String[] { "cane", "sugarcane", "sugarcanes"}),
    JUKEBOX(84, "Jukebox", new String[] { "stereo", "recordplayer"}),
    FENCE(85, "Fence"),
    PUMPKIN(86, "Pumpkin"),
    NETHERRACK(87, "Netherrack",
            new String[] {"redmossycobblestone", "redcobblestone", "redmosstone",
            "redcobble", "netherstone", "nether", "hellstone"}),
    SOUL_SAND(88, "Soul sand",
            new String[] {"slowmud", "mud","hellmud"}),
    GLOWSTONE(89, "Glowstone",
            new String[] {"brittlegold",  "lightstone", "brimstone", "australium"}),
    PORTAL(90, "Portal"),
    JACK_O_LANTERN(91, "Pumpkin (on)",
            new String[] {"pumpkinlighted", "pumpkinon", "litpumpkin", "jackolantern"}),
    CAKE(92, "Cake",  "cakeblock"),
    REDSTONE_REPEATER_OFF(93, "Redstone repeater (off)", new String[] {"diodeoff", "redstonerepeater", "repeater", "delayer"}),
    REDSTONE_REPEATER_ON(94, "Redstone repeater (on)", new String[] {"diode", "diodeon", "redstonerepeateron", "repeateron", "delayeron"}),
    LOCKED_CHEST(95, "Locked chest", new String[] { "steveco", "supplycrate", "valveneedstoworkonep3nottf2kthx"}),
    TRAP_DOOR(96,"Trap door"),

    // Items
    IRON_SHOVEL(256, "Iron shovel"),
    IRON_PICK(257, "Iron pick",  "ironpickaxe"),
    IRON_AXE(258, "Iron axe"),
    FLINT_AND_TINDER(259, "Flint and tinder",
            new String[] { "lighter", "flintandsteel", "flintsteel",
            "flintandiron", "flintnsteel", "flintniron", "flintntinder"}),
    RED_APPLE(260, "Red apple",  "apple"),
    BOW(261, "Bow"),
    ARROW(262, "Arrow"),
    COAL(263,0, "Coal", ChatColor.BLACK),
    CHARCOAL(263,1, "Charcoal",ChatColor.BLACK),
    DIAMOND(264,"Diamond",ChatColor.AQUA),
    IRON_BAR(265, "Iron bar", "ironingot"),
    GOLD_BAR(266, "Gold bar", "goldingot",ChatColor.GOLD),
    IRON_SWORD(267, "Iron sword"),
    WOOD_SWORD(268, "Wooden sword", "woodsword"),
    WOOD_SHOVEL(269, "Wooden shovel", "woodshovel"),
    WOOD_PICKAXE(270, "Wooden pickaxe", new String[] {"woodpick", "woodpickaxe"}),
    WOOD_AXE(271, "Wooden axe", "woodaxe"),
    STONE_SWORD(272, "Stone sword", "stonesword"),
    STONE_SHOVEL(273, "Stone shovel", "stoneshovel"),
    STONE_PICKAXE(274, "Stone pickaxe", "stonepick"),
    STONE_AXE(275, "Stone pickaxe", "stoneaxe"),
    DIAMOND_SWORD(276, "Diamond sword", ChatColor.AQUA),
    DIAMOND_SHOVEL(277, "Diamond shovel", "diamondshovel",ChatColor.AQUA),
    DIAMOND_PICKAXE(278, "Diamond pickaxe", new String[] {"diamondpick", "diamondpickaxe"},ChatColor.AQUA),
    DIAMOND_AXE(279, "Diamond axe", "diamondaxe",ChatColor.AQUA),
    STICK(280, "Stick", "stick"),
    BOWL(281, "Bowl", "bowl"),
    MUSHROOM_SOUP(282, "Mushroom soup", new String[] {"mushroomsoup", "soup", "brbsoup"}),
    GOLD_SWORD(283, "Golden sword", "goldsword",ChatColor.GOLD),
    GOLD_SHOVEL(284, "Golden shovel", "goldshovel",ChatColor.GOLD),
    GOLD_PICKAXE(285, "Golden pickaxe", new String[] {"goldpick", "goldpickaxe"},ChatColor.GOLD),
    GOLD_AXE(286, "Golden axe", "goldaxe",ChatColor.GOLD),
    STRING(287, "String"),
    FEATHER(288, "Feather"),
    SULPHUR(289, "Sulphur", new String[] {"sulfur", "gunpowder"}),
    WOOD_HOE(290, "Wooden hoe", "woodhoe"),
    STONE_HOE(291, "Stone hoe"),
    IRON_HOE(292, "Iron hoe"),
    DIAMOND_HOE(293, "Diamond hoe", "diamondhoe"),
    GOLD_HOE(294, "Golden hoe", "goldhoe",ChatColor.GOLD),
    SEEDS(295, "Seeds", "seed"),
    WHEAT(296, "Wheat"),
    BREAD(297, "Bread"),
    LEATHER_HELMET(298, "Leather helmet"),
    LEATHER_CHEST(299, "Leather chestplate", "leatherchest"),
    LEATHER_PANTS(300, "Leather pants", "leatherleggings"),
    LEATHER_BOOTS(301, "Leather boots", "leatherboots"),
    CHAINMAIL_HELMET(302, "Chainmail helmet"),
    CHAINMAIL_CHEST(303, "Chainmail chestplate", "chainmailchest"),
    CHAINMAIL_PANTS(304, "Chainmail pants", "chainmailleggings"),
    CHAINMAIL_BOOTS(305, "Chainmail boots"),
    IRON_HELMET(306, "Iron helmet"),
    IRON_CHEST(307, "Iron chestplate", "ironchest"),
    IRON_PANTS(308, "Iron pants", "ironleggings"),
    IRON_BOOTS(309, "Iron boots", "ironboots"),
    DIAMOND_HELMET(310, "Diamond helmet",ChatColor.AQUA),
    DIAMOND_CHEST(311, "Diamond chestplate", "diamondchest",ChatColor.AQUA),
    DIAMOND_PANTS(312, "Diamond pants", "diamondleggings",ChatColor.AQUA),
    DIAMOND_BOOTS(313, "Diamond boots", ChatColor.AQUA),
    GOLD_HELMET(314, "Gold helmet", "goldhelmet",ChatColor.GOLD),
    GOLD_CHEST(315, "Gold chestplate", "goldchest",ChatColor.GOLD),
    GOLD_PANTS(316, "Gold pants", "goldleggings",ChatColor.GOLD),
    GOLD_BOOTS(317, "Gold boots", ChatColor.GOLD),
    FLINT(318, "Flint"),
    RAW_PORKCHOP(319, "Raw porkchop",
            new String[] {"rawpork", "rawbacon", "baconstrips", "rawmeat"}),
    COOKED_PORKCHOP(320, "Cooked porkchop",
            new String[] {"pork", "cookedpork", "cookedbacon", "bacon", "meat"}),
    PAINTING(321, "Painting"),
    GOLD_APPLE(322, "Golden apple", "goldapple"),
    SIGN(323, "Wooden sign", "sign"),
    WOODEN_DOOR_ITEM(324, "Wooden door", new String[] {"wooddoor", "door"}),
    BUCKET(325, "Bucket", "bukkit"),
    WATER_BUCKET(326, "Water bucket", "waterbukkit"),
    LAVA_BUCKET(327, "Lava bucket", "lavabukkit"),
    MINECART(328, "Minecart", "cart"),
    SADDLE(329, "Saddle"),
    IRON_DOOR_ITEM(330, "Iron door"),
    REDSTONE_DUST(331, "Redstone dust", "reddust"),
    SNOWBALL(332, "Snowball"),
    WOOD_BOAT(333, "Wooden boat", new String[] {"woodboat", "boat"}),
    LEATHER(334, "Leather", "cowhide"),
    MILK_BUCKET(335, "Milk bucket", new String[] {"milk", "milkbukkit"}),
    BRICK_BAR(336, "Brick"),
    CLAY_BALL(337, "Clay"),
    SUGAR_CANE_ITEM(338, "Sugar cane", new String[] { "reed", "reeds"}),
    PAPER(339, "Paper"),
    BOOK(340, "Book"),
    SLIME_BALL(341, "Slime ball", "slime"),
    STORAGE_MINECART(342, "Storage minecart", "storagecart"),
    POWERED_MINECART(343, "Powered minecart", "poweredcart"),
    EGG(344, "Egg"),
    COMPASS(345, "Compass"),
    FISHING_ROD(346, "Fishing rod", "fishingpole"),
    WATCH(347, "Watch", new String[] {"clock", "timer" }),
    LIGHTSTONE_DUST(348, "Glowstone dust", new String[] { 
            "lightstonedust", "glowstonedone", "brightstonedust",
            "brittlegolddust", "brimstonedust"}),
    RAW_FISH(349, "Raw fish", "fish"),
    COOKED_FISH(350, "Cooked fish"),
    
    INK_SACK(351, 0,  "Ink sac", new String[] {"ink", "dye", "inksack"}, ChatColor.BLACK),
    ROSE_RED(351, 1,  "Rose Red", "reddye", ChatColor.DARK_RED),
    CACTUS_GREEN(351, 2,  "Cactus Green", "greendye", ChatColor.DARK_GREEN),
    COCOA_BEAN(351, 3,  "Cocoa Bean", new String[] {"cocoabeans","browndye"}),
    LAPIS_LAZULI(351, 4,  "Lapis Lazuli", "bluedye",ChatColor.BLUE),
    PURPLE_DYE(351, 5,  "Purple dye", ChatColor.DARK_PURPLE),
    CYAN_DYE(351, 6,  "Cyan dye", ChatColor.DARK_AQUA),
    LIGHT_GRAY_DYE(351, 7,  "Light gray dye", ChatColor.GRAY),
    GRAY_DYE(351, 8,  "Gray dye", ChatColor.GRAY),
    PINK_DYE(351, 9,  "Pink dye"),
    LIME_DYE(351, 10,  "Lime dye",ChatColor.GREEN),
    DANDELION_YELLOW(351, 11,  "Dandelion Yellow", "yellowdye",ChatColor.YELLOW),
    LIGHT_BLUE_DYE(351, 12,  "Light blue dye", ChatColor.BLUE),
    MAGENTA_DYE(351, 13,  "Magenta dye"),
    ORANGE_DYE(351, 14,  "Orange dye"),
    BONE_MEAL(351, 15,  "Bone Meal",ChatColor.WHITE),
    BONE(352, "Bone"),
    SUGAR(353, "Sugar"),
    CAKE_ITEM(354, "Cake"),
    BED_ITEM(355, "Bed"),
    REDSTONE_REPEATER(356, "Redstone repeater", new String[] { "diode", "delayer"}),
    COOKIE(357, "Cookie"),
    MAP(358, "Map"),
    SHEARS(359, "Shears", "sheepshears"),
    GOLD_RECORD(2256, "Gold Record", "golddisc"),
    GREEN_RECORD(2257, "Green Record", "greendisc");

    /**
     * Stores a map of the IDs for fast access.
     */
    private static final Map<Integer,ItemType> ids = new HashMap<Integer,ItemType>();
    /**
     * Stores a map of the names for fast access.
     */
    private static final Map<String,ItemType> lookup = new HashMap<String,ItemType>();

    private final int id;
    private final int dataValue;
    private final String nicename;
    private final String[] lookupKeys;
    private ChatColor color=ChatColor.BLACK;
    private static final List<String> woolColors = Arrays.asList("white","orange","magenta","lightblue","yellow","lightgreen","pink","gray","lightgray","cyan","purple","blue","brown","darkgreen","red","black");

    static {
        for (ItemType type : EnumSet.allOf(ItemType.class)) {
            ids.put((type.id * 1000) + type.dataValue, type);
            addLookup(type.nicename,type);
            addLookup(type.toString(),type);
            
            if (type.lookupKeys!=null) {
                for (String key : type.lookupKeys)  {
                    addLookup(key,type);
                }
            }
        }
    }

    private static void addLookup(String key,ItemType type) {
        lookup.put(key.toLowerCase(), type);
        lookup.put(key.toLowerCase().replaceAll("\\s+", ""), type);
        if (key.length()>15) {              
            lookup.put(key.toLowerCase().substring(0,15),type);
        }
    }
    
    ItemType(int id, int dataValue, String name, String[] lookupKeys, ChatColor color) {
        this.id = id;
        this.dataValue = dataValue;
        this.nicename = name;
        this.lookupKeys = lookupKeys;
        this.color = color;
    }
    
    ItemType(int id, int dataValue, String name, String lookupKey, ChatColor color) {
        this(id,dataValue,name,new String[] {lookupKey},color);
    }


    ItemType(int id, int dataValue, String name) {
        this.id = id;
        this.dataValue = dataValue;
        this.nicename = name;
        this.lookupKeys = null;
        this.color = null;
    }
    
    ItemType(int id, int dataValue, String name, ChatColor color) {
        this(id,dataValue,name);
        this.color = color;
    }

    ItemType(int id,  String name) {
        this.id = id;
        this.dataValue = 0;
        this.nicename = name;
        this.lookupKeys = null;
        this.color = null;
    }
    
    
    ItemType(int id,  String name, ChatColor color) {
        this(id,name);
        this.color = color;
    }
    
    ItemType(int id,  String name, String lookupKey) {
        this(id,0,name,lookupKey,null);
    }

    ItemType(int id, String name, String[] lookupKeys) {
        this(id,0,name,lookupKeys,null);
    }

    ItemType(int id, int dataValue, String name, String lookupKey) {
        this(id,dataValue,name,lookupKey,null);
    }

    ItemType(int id, int dataValue, String name, String[] lookupKeys) {
        this(id,dataValue,name,lookupKeys,null);
    }

    ItemType(int id,  String name, String lookupKey, ChatColor color) {
        this(id,0,name,lookupKey,color);
    }

    ItemType(int id, String name, String[] lookupKeys, ChatColor color) {
        this(id,0,name,lookupKeys,color);
    }


    
    
    /**
     * Return type from ID. May return null.
     *
     * @param id
     * @return
     */
    public static ItemType getFromID(int id) {
        return getFromID(id,0);
    }

    /**
     * Return type from ID and Datavalue. May return null.
     *
     * @param id
     * @param datavalue
     * @return
     */
    public static ItemType getFromID(int id,int dataValue) {
        return ids.get(id*1000+dataValue);
    }

    public static ItemType getFromBlock(Block block) {
        return ids.get(block.getTypeId()*1000+ block.getData());
    }
    
    
    /**
     * Return type from MaterialData
     * 
     * @param mat
     * @return ItemType representing material
     */
    public static ItemType fromMaterial(MaterialData mat) {
        return getFromID(mat.getItemTypeId(), mat.getData());
    }
    
    /**
     * Return type from ItemType
     * 
     * @param mat
     * @return ItemType representing material
     */
    public static ItemType fromItemStack(ItemStack item) {
        MaterialData data = item.getData();
        if (data==null)
            return getFromID(item.getTypeId());
        else        
            return getFromID(item.getData().getItemTypeId(),  item.getData().getData());
    }
    
    /**
     * Get a name for the item.
     *
     * @param id
     * @return
     */
    
    public static String toName(int id) {
        return toName(id,0);
    }

    /**
     * Get a name for the item.
     *
     * @param id
     * @param datavalue 
     * @return
     */
    public static String toName(int id, int dataValue) {
        ItemType type = ids.get(id*1000 + dataValue);
        if (type != null) {
            return type.getName();
        } else {
            return "#" + id;
        }
    }

    /**
     * Returns is an Id is know.
     *
     * @param id
     * @return
     */
    public static boolean isKnownId(int id)
    {
        return isKnownId(id,0);
    }

    /**
     * Returns is an Id is know.
     *
     * @param id
     * @return
     */
    public static boolean isKnownId(int id,int dataValue)
    {
        return getFromID(id,dataValue)!=null;
    }

    /**
     * Get a name for a held item.
     *
     * @param id
     * @return
     */
    public static String toHeldName(int id) {
        return toHeldName(id,0);
    }

    /**
     * Get a name for a held item.
     *
     * @param id
     * @return
     */
    public static String toHeldName(int id,int dataValue) {
        if (id == 0) {
            return "Hand";
        } else {
            return (toName(id,dataValue));
        }
    }

    /**
     * Return type from name. May return null.
     *
     * @param name
     * @return
     */
    public static ItemType lookup(String name) {
        return lookup.get(name.toLowerCase());
    }

    /**
     * Get item numeric ID.
     *
     * @return
     */
    public int getID() {
        return id;
    }

    public ItemStack getItemStack(int amount) {
        return new ItemStack(this.id,amount,(byte)this.dataValue);
    }
    
    /**
     * Get item numeric ID.
     *
     * @return
     */
    public int getDataValue() {
        return dataValue;
    }

    /**
     * Get user-friendly item name.
     *
     * @return
     */
    public String getName() {
        return nicename;
    }

    public String getColorString() {
        return ((color!=null)?color.toString():"");
    }
    
    public String getColoredName() {
        return getColorString() + nicename;
    }
    
    /**
     * Get a list of aliases.
     * 
     * @return
     */
    public String[] getAliases() {
        return lookupKeys;
    }

    /**
     * Returns true if an item should not be stacked.
     * 
     * @param id
     * @return
     */
    public static boolean shouldNotStack(int id) {
        return (id >= 256 && id <= 259)
        || id == 261
        || (id >= 267 && id <= 279)
        || (id >= 281 && id <= 286)
        || (id >= 290 && id <= 294)
        || (id >= 298 && id <= 317)
        || (id >= 325 && id <= 327)
        || id == 335
        || id == 354
        || id == 355
        || id >= 2256;
    }

    /**
     * Returns true if an item uses its damage value for something
     * other than damage.
     * 
     * @param id
     * @return
     */
    public static boolean usesDamageValue(int id) {
        return id == 35
        || id == 351;
    }

    /**
     * parses an itemstring and returns its corresponding ItemType.
     *   
     * @param itemString String representation of a block, can have a datavalue
     * @return ItemType or null if not recognised
     */
    public static ItemType parseItemString(String itemString) {

        String[] stringPart = itemString.toLowerCase().split(":");

        ItemType item = ItemType.lookup(stringPart[0]);

        if (item==null)
        {
            try {
                item=ItemType.getFromID(Integer.parseInt(stringPart[0]));               
            } catch (NumberFormatException c) {
                return null;
            }
        }

        
        if (item==null) {
            return null;
        }
        
        if (stringPart.length>1) {
            int dataValue = 0;
            if (item.equals(ItemType.WHITE_WOOL)) {
                dataValue = woolColors.indexOf(stringPart[1]);
                if (dataValue>=0)
                    return ItemType.getFromID(item.getID(), dataValue);
            } 
            try {
                dataValue = Integer.parseInt(stringPart[1]);
                if ((dataValue <0) || (dataValue>15))
                    return null;
            } catch (NumberFormatException e) {
                return null;
            }
            item = ItemType.getFromID(item.getID(), dataValue);
        }
        return item;
    }
    
    /**
     * parses an itemstring and returns its corresponding ItemType.
     *   
     * @param itemString String representation of a block, can have a datavalue
     * @return ItemType or defaultItem if not recognised
     */
    public static ItemType parseItemString(String itemString, ItemType defaultItem) {
        ItemType foundItem = parseItemString(itemString);
        return foundItem==null?defaultItem:foundItem;
    }
}