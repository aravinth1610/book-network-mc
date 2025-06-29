INSERT INTO eazybank.auth_routes (api_end_point,can_activate,can_deactivate,component,`path`,path_match,permission,redirect_to,fk_parent_auth_route,created_by,created_on,delete_flag,updated_by,updated_on) VALUES
	 ('/authmenu/**',NULL,NULL,'authmenuComp','authmenu',NULL,'POST,GET',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 ('/user/{id}','authGuard',NULL,'userComp','user',NULL,'GET',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 ('/user','authGuard',NULL,'userComp','user',NULL,'GET',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 ('/user/{id}','authGuard',NULL,'userComp','user',NULL,'DELETE',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 ('/user/{id}/role','authGuard',NULL,'userRoleComp','userRole',NULL,'POST',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 ('/orgId/{orgId}/user',NULL,NULL,'userComp','UserCreate',NULL,'POST',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 ('/details/{id}','authGuard',NULL,'','',NULL,'GET,POST',NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	 ('/details/**','authGuard',NULL,'BookInvComp','BookInv',NULL,'GET,POST',NULL,NULL,'anonymousUser','2025-04-06 15:39:35.665000',0,NULL,NULL);
