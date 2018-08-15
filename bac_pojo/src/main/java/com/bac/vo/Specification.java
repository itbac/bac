package com.bac.vo;

import com.bac.pojo.TbSpecification;
import com.bac.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {
    //封装规格pojo
    private TbSpecification specification;

    //规格选项
    //一个规格对应多个规格选项
    private List<TbSpecificationOption> specificationOptionList;


    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
