/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.supply.bpm.controller.model;

import com.alibaba.fastjson.JSON;
import com.supply.common.exception.ApiException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Map;

/**
 * @author wjd
 * @description 流程配置加载.
 * @date 2022-12-19
 */
@RestController
@RequestMapping("/bpm/act")
public class StencilsetRestResource {

  @RequestMapping(value="/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public @ResponseBody Map getStencilset() {
    InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("static/stencilset.json");
    try {
      final String config = IOUtils.toString(stencilsetStream, "utf-8");
      final Map map = JSON.parseObject(config, Map.class);
      return map;
    } catch (Exception e) {
      throw new ApiException("加载stencil配置出错", e);
    }
  }
}
