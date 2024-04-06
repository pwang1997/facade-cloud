package com.facade.facadecore.base.mapper;

import com.facade.facadecore.base.model.BaseBO;
import com.facade.facadecore.base.model.BaseDTO;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/22/2024
 */
public interface BaseMapper<B extends BaseBO, D extends BaseDTO> {
    D mapToDTO(B b);

    B mapToBO(D d);
}