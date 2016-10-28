package slh.capture.common;

public enum SqlName {
  /**
   * 查询命名空间下的单个实体对象
   */
  select_slh_model,

  /**
   * 查询命名空间下的实体对象集合
   */
  select_slh_model_list,

  /**
   * 根据名称查询对象
   */
  select_slh_model_by_name,

  /**
   * 插入命名空间下的实体对象
   */
  insert_slh_model,

  /**
   * 删除命名空间下的实体对象
   */
  delete_slh_model,

  /**
   * 修改命名空间下的实体对象
   */
  update_slh_model,

  /**
   * 统计总数
   */
  query_slh_count;
}
