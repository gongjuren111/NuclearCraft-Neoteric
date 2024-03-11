package igentuman.nc.content.energy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import igentuman.nc.NuclearCraft;
import igentuman.nc.block.entity.energy.NCEnergy;
import igentuman.nc.block.entity.energy.RTGBE;
import net.minecraft.tileentity.TileEntityType;

import static igentuman.nc.handler.config.CommonConfig.ENERGY_GENERATION;

public class RTGs {

    private static HashMap<String, RTGPrefab> all = new HashMap<>();
    private static HashMap<String, RTGPrefab> registered = new HashMap<>();

    public static HashMap<String, RTGPrefab> all() {
        if(all.isEmpty()) {
            //radiation in pRads
            all.put("uranium_rtg", new RTGPrefab("uranium_rtg",20, 56).setBlockEntity(null));
            all.put("americium_rtg", new RTGPrefab("americium_rtg",200, 57800).setBlockEntity(null));
            all.put("plutonium_rtg", new RTGPrefab("plutonium_rtg",800, 200000).setBlockEntity(null));
            all.put("californium_rtg", new RTGPrefab("californium_rtg",4000, 1900000).setBlockEntity(null));
        }
        return all;
    }

    public static HashMap<String, RTGPrefab> registered() {
        if(registered.isEmpty()) {
            for(String name: all().keySet()) {
                if (all().get(name).config().isRegistered())
                    registered.put(name,all().get(name));
            }
        }
        return registered;
    }

    public static List<Boolean> initialRegistered() {
        List<Boolean> tmp = new ArrayList<>();
        for(String name: all().keySet()) {
            tmp.add(true);
        }
        return tmp;
    }

    public static List<Integer> initialPower() {
        List<Integer> tmp = new ArrayList<>();
        for(String name: all().keySet()) {
            tmp.add(all().get(name).getGeneration());
        }
        return tmp;
    }

    public static List<Integer> initialRadiation() {
        List<Integer> tmp = new ArrayList<>();
        for(String name: all().keySet()) {
            tmp.add(all().get(name).getRadiation());
        }
        return tmp;
    }

    public static class RTGPrefab {
        private boolean registered = true;
        private boolean initialized = false;
        private final String name;
        protected int generation = 0;
        protected int radiation = 0;

        public RTGPrefab(String name, int generation, int radiation) {
            this.generation = generation;
            this.radiation = radiation;
            this.name = name;
        }

        public int getGeneration() {
            return generation;
        }

        public RTGPrefab setGeneration(int generation) {
            this.generation = generation;
            return this;
        }

        public RTGPrefab config()
        {
            if(!initialized) {
                try {
                    int id = Arrays.asList(RTGs.all().keySet().stream().toArray()).indexOf(name);
                    registered = ENERGY_GENERATION.REGISTER_RTG.get().get(id);
                    generation = ENERGY_GENERATION.RTG_GENERATION.get().get(id);
                    radiation = ENERGY_GENERATION.RTG_RADIATION.get().get(id);
                    initialized = true;
                } catch (Exception e) {
                    NuclearCraft.LOGGER.error("Error while loading config for " + name + "!");
                }
            }
            return this;
        }
        public boolean isRegistered() {
            return  registered;
        }

        public Supplier<? extends NCEnergy> getBlockEntity(String name) {
            return () -> new RTGBE(name);
        }

        public RTGPrefab setBlockEntity(Supplier<? extends NCEnergy>  blockEntity) {
            this.blockEntity = blockEntity;
            return this;
        }
        private Supplier<? extends NCEnergy>  blockEntity;

        public int getRadiation() {
            return radiation;
        }
    }
}
