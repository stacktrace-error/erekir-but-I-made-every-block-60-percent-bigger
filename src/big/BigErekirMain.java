package big;

import arc.struct.Seq;
import arc.util.*;
import big.content.BigErekirBlocks;
import mindustry.*;
import mindustry.mod.*;
import rhino.ImporterTopLevel;
import rhino.NativeJavaPackage;

public class BigErekirMain extends Mod{

    public BigErekirMain(){}

    @Override
    public void loadContent(){
        BigErekirBlocks.load();
    }

    public static NativeJavaPackage p = null;

    @Override
    public void init() {
        super.init();
        ImporterTopLevel scope = (ImporterTopLevel) Vars.mods.getScripts().scope;

        Seq<String> packages = Seq.with(
                "erekir-big",
                "erekir-big.types",
                "erekir-big.content"
        );

        packages.each(name -> {
            p = new NativeJavaPackage(name, Vars.mods.mainLoader());
            p.setParentScope(scope);
            scope.importPackage(p);
        });
    }
}
