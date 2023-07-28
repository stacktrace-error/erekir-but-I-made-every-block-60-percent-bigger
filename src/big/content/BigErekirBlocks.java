package big.content;

import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.core.UI;
import mindustry.entities.effect.RadialEffect;
import mindustry.gen.Sounds;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.blocks.defense.AutoDoor;
import mindustry.world.blocks.defense.ShieldWall;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.heat.HeatProducer;
import mindustry.world.blocks.logic.CanvasBlock;
import mindustry.world.blocks.logic.MessageBlock;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.production.BeamDrill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.HeatCrafter;
import mindustry.world.blocks.production.ItemIncinerator;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitAssembler;
import mindustry.world.blocks.units.UnitAssemblerModule;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.draw.*;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;

import static mindustry.type.ItemStack.with;

@SuppressWarnings("unused")
public class BigErekirBlocks{
    public static Seq<Block> blocks = new Seq<>(), exceptions = new Seq<>();

    public static Block
    siliconArcFurnace, electrolyzer, oxidationChamber, atmosphericConcentrator, electricHeater, slagHeater, phaseHeater, heatRedirector, heatRouter, slagIncinerator,
    carbideCrucible, slagCentrifuge, surgeCrucible, cyanogenSynthesizer, phaseSynthesizer, heatReactor,

    berylliumWall, berylliumWallLarge, tungstenWall, tungstenWallLarge, blastDoor, reinforcedSurgeWall, reinforcedSurgeWallLarge, carbideWall, carbideWallLarge,
    shieldedWall,

    radar, buildTower, regenProjector, barrierProjector, shockwaveTower,

    unitCargoLoader, unitCargoUnloadPoint,

    reinforcedPump, reinforcedLiquidContainer, reinforcedLiquidTank,

    turbineCondenser, ventCondenser, chemicalCombustionChamber, pyrolysisGenerator, fluxReactor, neoplasiaReactor,
    beamNode, beamTower, beamLink,

    cliffCrusher, plasmaBore, largePlasmaBore, impactDrill, eruptionDrill,

    coreBastion, coreCitadel, coreAcropolis, reinforcedContainer, reinforcedVault,

    breach, diffuse, sublimate, titan, disperse, afflict, lustre, scathe, smite, malign,

    tankFabricator, shipFabricator, mechFabricator,
    tankRefabricator, shipRefabricator, mechRefabricator,
    primeRefabricator,
    tankAssembler, shipAssembler, mechAssembler,
    basicAssemblerModule,
    unitRepairTower,

    reinforcedPayloadConveyor, reinforcedPayloadRouter, payloadMassDriver, largePayloadMassDriver, smallDeconstructor, deconstructor, constructor, largeConstructor, payloadLoader, payloadUnloader,

    canvas, reinforcedMessage;

    public static void load(){
        siliconArcFurnace = new GenericCrafter("large-silicon-arc-furnace"){{
            requirements(Category.crafting, with(Items.beryllium, 70, Items.graphite, 80));
            craftEffect = Fx.none;
            outputItem = new ItemStack(Items.silicon, 4);
            craftTime = 50f;
            size = 5;
            hasPower = true;
            hasLiquids = false;
            envEnabled |= Env.space | Env.underwater;
            envDisabled = Env.none;
            itemCapacity = 30;

            consumeItems(with(Items.graphite, 1, Items.sand, 4));
            consumePower(6f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawArcSmelt(), new DrawDefault());
            fogRadius = 3;
            researchCost = with(Items.beryllium, 150, Items.graphite, 50);
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.12f;
        }};

        electrolyzer = new GenericCrafter("large-electrolyzer"){{
            requirements(Category.crafting, with(Items.silicon, 50, Items.graphite, 40, Items.beryllium, 130, Items.tungsten, 80));
            size = 5;

            researchCostMultiplier = 1.2f;
            craftTime = 10f;
            rotate = true;
            invertFlip = true;
            group = BlockGroup.liquids;

            liquidCapacity = 50f;

            consumeLiquid(Liquids.water, 10f / 60f);
            consumePower(1f);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(Liquids.water, 4f),
                    new DrawBubbles(Color.valueOf("7693e3")){{
                        sides = 10;
                        recurrence = 3f;
                        spread = 6;
                        radius = 1.5f;
                        amount = 20;
                    }},
                    new DrawRegion(),
                    new DrawLiquidOutputs(),
                    new DrawGlowRegion(){{
                        alpha = 0.7f;
                        color = Color.valueOf("c4bdf3");
                        glowIntensity = 0.3f;
                        glowScale = 6f;
                    }}
            );

            ambientSound = Sounds.electricHum;
            ambientSoundVolume = 0.08f;

            regionRotated1 = 3;
            outputLiquids = LiquidStack.with(Liquids.ozone, 4f / 60, Liquids.hydrogen, 6f / 60);
            liquidOutputDirections = new int[]{1, 3};
        }};

        oxidationChamber = new HeatProducer("large-oxidation-chamber"){{
            requirements(Category.crafting, with(Items.tungsten, 120, Items.graphite, 80, Items.silicon, 100, Items.beryllium, 120));
            size = 5;

            outputItem = new ItemStack(Items.oxide, 1);
            researchCostMultiplier = 1.1f;

            hasLiquids = true;
            consumeLiquid(Liquids.ozone, 2f / 60f);
            consumeItem(Items.beryllium);
            consumePower(0.5f);

            rotateDraw = false;

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidRegion(), new DrawDefault(), new DrawHeatOutput());
            ambientSound = Sounds.extractLoop;
            ambientSoundVolume = 0.08f;

            regionRotated1 = 2;
            craftTime = 60f * 2f;
            liquidCapacity = 30f;
            heatOutput = 5f;
        }};

        atmosphericConcentrator = new HeatCrafter("large-atmospheric-concentrator"){{
            requirements(Category.crafting, with(Items.oxide, 60, Items.beryllium, 180, Items.silicon, 150));
            size = 5;
            hasLiquids = true;

            consumePower(2f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.nitrogen, 4.1f), new DrawDefault(), new DrawHeatInput(),
                    new DrawParticles(){{
                        color = Color.valueOf("d4f0ff");
                        alpha = 0.6f;
                        particleSize = 4f;
                        particles = 10;
                        particleRad = 12f;
                        particleLife = 140f;
                    }});

            researchCostMultiplier = 1.1f;
            liquidCapacity = 40f;
            ambientSound = Sounds.extractLoop;
            ambientSoundVolume = 0.06f;

            heatRequirement = 6f;

            outputLiquid = new LiquidStack(Liquids.nitrogen, 4f / 60f);

            researchCost = with(Items.silicon, 2000, Items.oxide, 900, Items.beryllium, 2400);
        }};

        electricHeater = new HeatProducer("large-electric-heater"){{
            requirements(Category.crafting, with(Items.tungsten, 30, Items.oxide, 30));

            researchCostMultiplier = 4f;

            consumePower(100f / 60f);

            drawer = new DrawMulti(new DrawDefault(), new DrawHeatOutput());
            rotateDraw = false;
            size = 3;
            heatOutput = 3f;
            regionRotated1 = 1;
            ambientSound = Sounds.hum;
            itemCapacity = 0;
        }};

        phaseHeater = new HeatProducer("large-phase-heater"){{
            requirements(Category.crafting, with(Items.oxide, 30, Items.carbide, 30, Items.beryllium, 30));

            drawer = new DrawMulti(new DrawDefault(), new DrawHeatOutput());
            size = 3;
            heatOutput = 15f;
            craftTime = 60f * 8f;
            ambientSound = Sounds.hum;
            consumeItem(Items.phaseFabric);
        }};

        slagIncinerator = new ItemIncinerator("large-slag-incinerator"){{
            requirements(Category.crafting, with(Items.tungsten, 15));
            size = 2;
            consumeLiquid(Liquids.slag, 2f / 60f);
        }};

        carbideCrucible = new HeatCrafter("large-carbide-crucible"){{
            requirements(Category.crafting, with(Items.tungsten, 110, Items.thorium, 150, Items.oxide, 60));
            craftEffect = Fx.none;
            outputItem = new ItemStack(Items.carbide, 1);
            craftTime = 60f * 2.25f;
            size = 5;
            itemCapacity = 20;
            hasPower = hasItems = true;

            consumeItems(with(Items.tungsten, 2, Items.graphite, 3));
            consumePower(2f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawCrucibleFlame(), new DrawDefault(), new DrawHeatInput());
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.09f;

            heatRequirement = 10f;
        }};

        surgeCrucible = new HeatCrafter("large-surge-crucible"){{
            requirements(Category.crafting, with(Items.silicon, 100, Items.graphite, 80, Items.tungsten, 80, Items.oxide, 80));

            size = 5;

            itemCapacity = 20;
            heatRequirement = 10f;
            craftTime = 60f * 3f;
            liquidCapacity = 80f * 5;

            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.9f;

            outputItem = new ItemStack(Items.surgeAlloy, 1);

            craftEffect = new RadialEffect(Fx.surgeCruciSmoke, 4, 90f, 5f);

            hasLiquids = true;
            consumeItem(Items.silicon, 3);
            consumeLiquid(Liquids.slag, 40f / 60f);
            consumePower(2f);

            drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawCircles(){{
                color = Color.valueOf("ffc073").a(0.24f);
                strokeMax = 2.5f;
                radius = 10f;
                amount = 3;
            }}, new DrawLiquidRegion(Liquids.slag), new DrawDefault(), new DrawHeatInput(),
                    new DrawHeatRegion(){{
                        color = Color.valueOf("ff6060ff");
                    }},
                    new DrawHeatRegion("-vents"){{
                        color.a = 1f;
                    }});
        }};

        phaseSynthesizer = new HeatCrafter("large-phase-synthesizer"){{
            requirements(Category.crafting, with(Items.carbide, 90, Items.silicon, 100, Items.thorium, 100, Items.tungsten, 200));

            size = 5;

            itemCapacity = 40;
            heatRequirement = 8f;
            craftTime = 60f * 2f;
            liquidCapacity = 10f * 4;

            ambientSound = Sounds.techloop;
            ambientSoundVolume = 0.04f;

            outputItem = new ItemStack(Items.phaseFabric, 1);

            consumeItems(with(Items.thorium, 2, Items.sand, 6));
            consumeLiquid(Liquids.ozone, 2f / 60f);
            consumePower(8f);

            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawSpikes(){{
                        color = Color.valueOf("ffd59e");
                        stroke = 3f;
                        layers = 2;
                        amount = 12;
                        rotateSpeed = 0.5f;
                        layerSpeed = -0.9f;
                        radius = 13.5f;
                    }},
                    new DrawMultiWeave(){{
                        glowColor = new Color(1f, 0.4f, 0.4f, 0.8f);
                    }},
                    new DrawDefault(),
                    new DrawHeatInput(),
                    new DrawHeatRegion("-vents"){{
                        color = new Color(1f, 0.4f, 0.3f, 1f);
                    }}
            );
        }};



        int wallHealthMultiplier = 4;

        berylliumWall = new Wall("large-beryllium-wall"){{
            requirements(Category.defense, with(Items.beryllium, 6));
            health = 130 * wallHealthMultiplier;
            armor = 2f;
            buildCostMultiplier = 8f;
            size = 2;
        }};

        berylliumWallLarge = new Wall("large-beryllium-wall-large"){{
            requirements(Category.defense, ItemStack.mult(berylliumWall.requirements, 4));
            health = 130 * wallHealthMultiplier * 4;
            armor = 2f;
            buildCostMultiplier = 5f;
            size = 3;
        }};

        tungstenWall = new Wall("large-tungsten-wall"){{
            requirements(Category.defense, with(Items.tungsten, 6));
            health = 180 * wallHealthMultiplier;
            armor = 14f;
            buildCostMultiplier = 8f;
            size = 2;
        }};

        tungstenWallLarge = new Wall("large-tungsten-wall-large"){{
            requirements(Category.defense, ItemStack.mult(tungstenWall.requirements, 4));
            health = 180 * wallHealthMultiplier * 4;
            armor = 14f;
            buildCostMultiplier = 5f;
            size = 3;
        }};

        blastDoor = new AutoDoor("large-blast-door"){{
            requirements(Category.defense, with(Items.tungsten, 24, Items.silicon, 24));
            health = 175 * wallHealthMultiplier * 4;
            armor = 14f;
            size = 3;
        }};

        reinforcedSurgeWall = new Wall("large-reinforced-surge-wall"){{
            requirements(Category.defense, with(Items.surgeAlloy, 6, Items.tungsten, 2));
            health = 250 * wallHealthMultiplier;
            lightningChance = 0.05f;
            lightningDamage = 30f;
            armor = 20f;
            researchCost = with(Items.surgeAlloy, 20, Items.tungsten, 100);
            size = 2;
        }};

        reinforcedSurgeWallLarge = new Wall("large-reinforced-surge-wall-large"){{
            requirements(Category.defense, ItemStack.mult(reinforcedSurgeWall.requirements, 4));
            health = 250 * wallHealthMultiplier * 4;
            lightningChance = 0.05f;
            lightningDamage = 30f;
            armor = 20f;
            size = 3;
            researchCost = with(Items.surgeAlloy, 40, Items.tungsten, 200);
        }};

        carbideWall = new Wall("large-carbide-wall"){{
            requirements(Category.defense, with(Items.thorium, 6, Items.carbide, 6));
            health = 270 * wallHealthMultiplier;
            armor = 16f;
            size = 2;
        }};

        carbideWallLarge = new Wall("large-carbide-wall-large"){{
            requirements(Category.defense, ItemStack.mult(carbideWall.requirements, 4));
            health = 270 * wallHealthMultiplier * 4;
            armor = 16f;
            size = 3;
        }};

        shieldedWall = new ShieldWall("large-shielded-wall"){{
            requirements(Category.defense, ItemStack.with(Items.phaseFabric, 20, Items.surgeAlloy, 12, Items.beryllium, 12));
            consumePower(3f / 60f);

            outputsPower = false;
            hasPower = true;
            consumesPower = true;
            conductivePower = true;

            chanceDeflect = 8f;

            health = 260 * wallHealthMultiplier * 4;
            armor = 15f;
            size = 3;
        }};

        plasmaBore = new BeamDrill("llarge-plasma-bore"){{
            requirements(Category.production, with(Items.beryllium, 40));
            consumePower(0.15f);

            drillTime = 160f;
            tier = 3;
            size = 3;
            range = 5;
            fogRadius = 3;
            researchCost = with(Items.beryllium, 10);

            consumeLiquid(Liquids.hydrogen, 0.25f / 60f).boost();
        }};


        tankFabricator = new UnitFactory("large-tank-fabricator"){{
            requirements(Category.units, with(Items.silicon, 200, Items.beryllium, 150));
            size = 5;
            configurable = false;
            plans.add(new UnitPlan(UnitTypes.stell, 60f * 35f, with(Items.beryllium, 40, Items.silicon, 50)));
            researchCost = with(Items.beryllium, 200, Items.graphite, 80, Items.silicon, 80);
            regionSuffix = "-dark";
            fogRadius = 5;
            consumePower(2f);
        }};

        shipFabricator = new UnitFactory("large-ship-fabricator"){{
            requirements(Category.units, with(Items.silicon, 250, Items.beryllium, 200));

            size = 5;
            configurable = false;
            plans.add(new UnitPlan(UnitTypes.elude, 60f * 40f, with(Items.graphite, 50, Items.silicon, 70)));
            regionSuffix = "-dark";
            fogRadius = 5;
            researchCostMultiplier = 0.5f;
            consumePower(2f);
        }};

        mechFabricator = new UnitFactory("large-mech-fabricator"){{
            requirements(Category.units, with(Items.silicon, 200, Items.graphite, 300, Items.tungsten, 60));
            size = 5;
            configurable = false;
            plans.add(new UnitPlan(UnitTypes.merui, 60f * 40f, with(Items.beryllium, 50, Items.silicon, 70)));
            regionSuffix = "-dark";
            fogRadius = 5;
            researchCostMultiplier = 0.65f;
            consumePower(2f);
        }};

        tankRefabricator = new Reconstructor("large-tank-refabricator"){{
            requirements(Category.units, with(Items.beryllium, 200, Items.tungsten, 80, Items.silicon, 100));
            regionSuffix = "-dark";

            size = 5;
            consumePower(3f);
            consumeLiquid(Liquids.hydrogen, 3f / 60f);
            consumeItems(with(Items.silicon, 40, Items.tungsten, 30));

            constructTime = 60f * 30f;
            researchCostMultiplier = 0.75f;

            upgrades.addAll(
                    new UnitType[]{UnitTypes.stell, UnitTypes.locus}
            );
        }};

        mechRefabricator = new Reconstructor("large-mech-refabricator"){{
            requirements(Category.units, with(Items.beryllium, 250, Items.tungsten, 120, Items.silicon, 150));
            regionSuffix = "-dark";

            size = 5;
            consumePower(2.5f);
            consumeLiquid(Liquids.hydrogen, 3f / 60f);
            consumeItems(with(Items.silicon, 50, Items.tungsten, 40));

            constructTime = 60f * 45f;
            researchCostMultiplier = 0.75f;

            upgrades.addAll(
                    new UnitType[]{UnitTypes.merui, UnitTypes.cleroi}
            );
        }};

        shipRefabricator = new Reconstructor("large-ship-refabricator"){{
            requirements(Category.units, with(Items.beryllium, 200, Items.tungsten, 100, Items.silicon, 150, Items.oxide, 40));
            regionSuffix = "-dark";

            size = 5;
            consumePower(2.5f);
            consumeLiquid(Liquids.hydrogen, 3f / 60f);
            consumeItems(with(Items.silicon, 60, Items.tungsten, 40));

            constructTime = 60f * 50f;

            upgrades.addAll(
                    new UnitType[]{UnitTypes.elude, UnitTypes.avert}
            );

            researchCost = with(Items.beryllium, 500, Items.tungsten, 200, Items.silicon, 300, Items.oxide, 80);
        }};

        //yes very silly name
        primeRefabricator = new Reconstructor("large-prime-refabricator"){{
            requirements(Category.units, with(Items.thorium, 250, Items.oxide, 200, Items.tungsten, 200, Items.silicon, 400));
            regionSuffix = "-dark";

            researchCostMultipliers.put(Items.thorium, 0.2f);

            size = 9;
            consumePower(5f);
            consumeLiquid(Liquids.nitrogen, 10f / 60f);
            consumeItems(with(Items.thorium, 80, Items.silicon, 100));

            constructTime = 60f * 60f;

            upgrades.addAll(
                    new UnitType[]{UnitTypes.locus, UnitTypes.precept},
                    new UnitType[]{UnitTypes.cleroi, UnitTypes.anthicus},
                    new UnitType[]{UnitTypes.avert, UnitTypes.obviate}
            );
        }};

        tankAssembler = new UnitAssembler("large-tank-assembler"){{
            requirements(Category.units, with(Items.thorium, 500, Items.oxide, 150, Items.carbide, 80, Items.silicon, 500));
            regionSuffix = "-dark";
            size = 9;
            plans.add(
                    new AssemblerUnitPlan(UnitTypes.vanquish, 60f * 50f, PayloadStack.list(UnitTypes.stell, 4, tungstenWallLarge, 10)),
                    new AssemblerUnitPlan(UnitTypes.conquer, 60f * 60f * 3f, PayloadStack.list(UnitTypes.locus, 6, carbideWallLarge, 20))
            );
            areaSize = 21;
            researchCostMultiplier = 0.4f;

            consumePower(3f);
            consumeLiquid(Liquids.cyanogen, 9f / 60f);
        }};

        shipAssembler = new UnitAssembler("large-ship-assembler"){{
            requirements(Category.units, with(Items.carbide, 100, Items.oxide, 200, Items.tungsten, 500, Items.silicon, 800, Items.thorium, 400));
            regionSuffix = "-dark";
            size = 9;
            plans.add(
                    new AssemblerUnitPlan(UnitTypes.quell, 60f * 60f, PayloadStack.list(UnitTypes.elude, 4, berylliumWallLarge, 12)),
                    new AssemblerUnitPlan(UnitTypes.disrupt, 60f * 60f * 3f, PayloadStack.list(UnitTypes.avert, 6, carbideWallLarge, 20))
            );
            areaSize = 21;

            consumePower(3f);
            consumeLiquid(Liquids.cyanogen, 12f / 60f);
        }};

        mechAssembler = new UnitAssembler("large-mech-assembler"){{
            requirements(Category.units, with(Items.carbide, 200, Items.thorium, 600, Items.oxide, 200, Items.tungsten, 500, Items.silicon, 900));
            regionSuffix = "-dark";
            size = 9;
            plans.add(
                    new AssemblerUnitPlan(UnitTypes.tecta, 60f * 70f, PayloadStack.list(UnitTypes.merui, 5, tungstenWallLarge, 12)),
                    new AssemblerUnitPlan(UnitTypes.collaris, 60f * 60f * 3f, PayloadStack.list(UnitTypes.cleroi, 6, carbideWallLarge, 20))
            );
            areaSize = 21;

            consumePower(3.5f);
            consumeLiquid(Liquids.cyanogen, 12f / 60f);
        }};

        basicAssemblerModule = new UnitAssemblerModule("large-basic-assembler-module"){{
            requirements(Category.units, with(Items.carbide, 300, Items.thorium, 500, Items.oxide, 200, Items.phaseFabric, 400));
            consumePower(4f);
            regionSuffix = "-dark";
            researchCostMultiplier = 0.75f;

            size = 9;
        }};

        reinforcedPayloadConveyor = new PayloadConveyor("large-reinforced-payload-conveyor"){{
            requirements(Category.units, with(Items.tungsten, 10));
            moveTime = 35f;
            canOverdrive = false;
            health = 800;
            researchCostMultiplier = 4f;
            underBullets = true;
            size = 5;
        }};

        reinforcedPayloadRouter = new PayloadRouter("large-reinforced-payload-router"){{
            requirements(Category.units, with(Items.tungsten, 15));
            moveTime = 35f;
            health = 800;
            canOverdrive = false;
            researchCostMultiplier = 4f;
            underBullets = true;
            size = 5;
        }};

        payloadMassDriver = new PayloadMassDriver("llarge-payload-mass-driver"){{
            requirements(Category.units, with(Items.tungsten, 120, Items.silicon, 120, Items.graphite, 50));
            regionSuffix = "-dark";
            size = 5;
            reload = 130f;
            chargeTime = 90f;
            range = 700f;
            maxPayloadSize = 4f; //todo unit specific max sizes for extra pain in ass?
            fogRadius = 5;
            consumePower(0.5f);
        }};

        largePayloadMassDriver = new PayloadMassDriver("large-large-payload-mass-driver"){{
            requirements(Category.units, with(Items.thorium, 200, Items.tungsten, 200, Items.silicon, 200, Items.graphite, 100, Items.oxide, 30));
            regionSuffix = "-dark";
            size = 9;
            reload = 130f;
            chargeTime = 100f;
            range = 1100f;
            maxPayloadSize = 5.6f;
            consumePower(3f);
        }};

        smallDeconstructor = new PayloadDeconstructor("large-small-deconstructor"){{
            requirements(Category.units, with(Items.beryllium, 100, Items.silicon, 100, Items.oxide, 40, Items.graphite, 80));
            regionSuffix = "-dark";
            itemCapacity = 100;
            consumePower(1f);
            size = 5;
            deconstructSpeed = 1f;
        }};

        deconstructor = new PayloadDeconstructor("large-deconstructor"){{
            requirements(Category.units, with(Items.beryllium, 250, Items.oxide, 100, Items.silicon, 250, Items.carbide, 250));
            regionSuffix = "-dark";
            itemCapacity = 250;
            consumePower(3f);
            size = 9;
            deconstructSpeed = 2f;
        }};

        constructor = new Constructor("llarge-constructor"){{
            requirements(Category.units, with(Items.silicon, 100, Items.beryllium, 150, Items.tungsten, 80));
            regionSuffix = "-dark";
            hasPower = true;
            buildSpeed = 0.6f;
            consumePower(2f);
            size = 5;
            filter = Seq.with(tungstenWallLarge, berylliumWallLarge, carbideWallLarge, reinforcedSurgeWallLarge, Blocks.reinforcedLiquidContainer, Blocks.reinforcedContainer, Blocks.beamNode);
            maxBlockSize = 4;
            minBlockSize = 3;
        }};

        //yes this block is pretty much useless
        largeConstructor = new Constructor("large-large-constructor"){{
            requirements(Category.units, with(Items.silicon, 150, Items.oxide, 150, Items.tungsten, 200, Items.phaseFabric, 40));
            regionSuffix = "-dark";
            hasPower = true;
            buildSpeed = 0.75f;
            maxBlockSize = 7;
            minBlockSize = 5;
            size = 9;

            consumePower(2f);
        }};

        payloadLoader = new PayloadLoader("large-payload-loader"){{
            requirements(Category.units, with(Items.graphite, 50, Items.silicon, 50, Items.tungsten, 80));
            regionSuffix = "-dark";
            hasPower = true;
            consumePower(2f);
            size = 5;
            fogRadius = 9;
        }};

        payloadUnloader = new PayloadUnloader("large-payload-unloader"){{
            requirements(Category.units, with(Items.graphite, 50, Items.silicon, 50, Items.tungsten, 30));
            regionSuffix = "-dark";
            hasPower = true;
            consumePower(2f);
            size = 5;
            fogRadius = 9;
        }};


        canvas = new CanvasBlock("large-canvas"){{
            requirements(Category.logic, BuildVisibility.shown, with(Items.silicon, 30, Items.beryllium, 10));

            canvasSize = 18;
            padding = 7f / 4f * 2f;

            size = 3;
        }};

        reinforcedMessage = new MessageBlock("large-reinforced-message"){{
            requirements(Category.logic, with(Items.graphite, 10, Items.beryllium, 5));
            health = 100;
            size = 2;
        }};





        blocks.addAll(
                siliconArcFurnace, electrolyzer, oxidationChamber, atmosphericConcentrator, electricHeater, slagIncinerator,
                carbideCrucible, phaseSynthesizer,

                berylliumWall, berylliumWallLarge, tungstenWall, tungstenWallLarge, blastDoor, reinforcedSurgeWall, reinforcedSurgeWallLarge, carbideWall, carbideWallLarge,
                shieldedWall,

                tankFabricator, shipFabricator, mechFabricator,
                tankRefabricator, shipRefabricator, mechRefabricator,
                primeRefabricator,
                tankAssembler, shipAssembler, mechAssembler,
                basicAssemblerModule,

                reinforcedPayloadConveyor, reinforcedPayloadRouter, largePayloadMassDriver, smallDeconstructor, deconstructor, largeConstructor, payloadLoader, payloadUnloader,

                canvas, reinforcedMessage
        );

        exceptions.addAll(
                plasmaBore, payloadMassDriver, constructor
        );

        blocks.each(block -> {
            Block b = Vars.content.block(block.name.substring("erekir-big-large-".length()));

            block.localizedName = b.localizedName;
            block.description = b.description;
            b.buildVisibility = BuildVisibility.sandboxOnly;
        });

        exceptions.each(block -> {
            Block b = Vars.content.block(block.name.substring("erekir-big-llarge-".length()));

            block.localizedName = b.localizedName;
            block.description = b.description;
            b.buildVisibility = BuildVisibility.sandboxOnly;
        });

        blocks.add(exceptions);

        Log.info("Big Erekir completion percentage: [purple]" + UI.formatAmount((long)((blocks.size / 61f) * 100f)) + "%[]");
    }
}
