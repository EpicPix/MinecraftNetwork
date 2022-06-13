package ga.epicpix.network.common.annotations;

import ga.epicpix.network.common.modules.ModulePermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface ChecksPermission {
    ModulePermission[] value();
}
