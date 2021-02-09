package com.zyrox.model.option;

import com.zyrox.model.option.impl.DefaultInterfaceOption;

/**
 * Created by Jonny on 6/22/2019
 **/
public class InterfaceOptionManager {

    public static InterfaceOption getOption(int interfaceId, DefaultInterfaceOption defaultInterfaceOption) {
        for(InterfaceOptionType interfaceOptionType : InterfaceOptionType.VALUES) {
            if(interfaceOptionType.getInterfaceId() == interfaceId) {
                return interfaceOptionType.getOptionValues()[defaultInterfaceOption.ordinal()];
            }
        }
        return null;
    }
}
