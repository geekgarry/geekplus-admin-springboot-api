-- 添加菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${title}', '3', '1', '${businessName}', '${moduleName}/${businessName}/index', 1, 'M', '0', '${permissionPrefix}:list', '#', 'gp', sysdate(), '', null, '${title}菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 添加按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${title}查询', @parentId, '1',  '#', '', 1,  'B', '0', '${permissionPrefix}:query', '#', 'gp', sysdate(), '', null, '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${title}新增', @parentId, '2',  '#', '', 1,  'B', '0', '${permissionPrefix}:add', '#', 'gp', sysdate(), '', null, '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${title}修改', @parentId, '3',  '#', '', 1,  'B', '0', '${permissionPrefix}:edit', '#', 'gp', sysdate(), '', null, '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${title}删除', @parentId, '4',  '#', '', 1,  'B', '0', '${permissionPrefix}:remove', '#', 'gp', sysdate(), '', null, '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${title}导出', @parentId, '5',  '#', '', 1,  'B', '0', '${permissionPrefix}:export', '#', 'gp', sysdate(), '', null, '');
