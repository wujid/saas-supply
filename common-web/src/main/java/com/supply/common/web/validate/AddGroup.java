package com.supply.common.web.validate;

import javax.validation.groups.Default;

/**
 * @author wjd
 * @description 此接口只用于validated的分组验证功能.
 * 只验证分组名为AddGroup或者默认类型,一般用于新增验证
 * @date 2022-12-05
 */
public interface AddGroup extends Default {
}
