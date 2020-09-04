package com.cjit.vms.metlife.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.metlife.model.SpecialRegIster;
import com.cjit.vms.metlife.service.RegIsterListService;

public class RegIsterListServiceImpl extends GenericServiceImpl implements RegIsterListService {

    @Override
    public List getRegIsterList(SpecialRegIster ister, PaginationList paginationList, boolean flag) {
        Map map = new HashMap();
        if (ister == null) {
            ister = new SpecialRegIster();
        }
        if (ister.getLstAuthInstId() != null && ister.getLstAuthInstId().size() > 0) {
            List instIds = ister.getLstAuthInstId();
            List lstTmp = new ArrayList();
            for (int i = 0; i < instIds.size(); i++) {
                Organization org = (Organization) instIds.get(i);
                lstTmp.add(org.getId());
            }
            map.put("auth_inst_ids", lstTmp);
        }
        map.put("ister", ister);
        if (flag == false) {
            map.put("cherNum", ister.getCherNum());
        }
        return this.find("getRegIsterList", map, paginationList);
    }

    @Override
    public void saveRegIsterList(SpecialRegIster ister, boolean flag) {
        Map map = new HashMap();
        map.put("ister", ister);
        if (flag) {
            this.save("saveRegIsterList", map);
        }
        if (flag == false) {
            this.update("upDateRegIsterList", map);
        }
    }

    @Override
    public void delRegIsterList(SpecialRegIster ister) {
        Map map = new HashMap();
        map.put("ister", ister);
        this.delete("delRegIsterList", map);
    }


}
