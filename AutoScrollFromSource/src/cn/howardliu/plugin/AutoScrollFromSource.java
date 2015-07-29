package cn.howardliu.plugin;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Set keyboard shortcut for the function "Scroll from Source" in the Project Panel.
 * <br/>create at 15-7-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class AutoScrollFromSource extends AnAction {
    private final Logger logger = Logger.getInstance(getClass());

    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            Project project = event.getProject();
            assert project != null;
            ProjectViewImpl projectView = (ProjectViewImpl) ProjectView.getInstance(project);
            Class<ProjectViewImpl> clazz = ProjectViewImpl.class;
            Field[] fields = clazz.getDeclaredFields();
            Field field = null;
            for (Field f : fields) {
                if ("MyAutoScrollFromSourceHandler".equals(f.getType().getSimpleName())) {
                    field = f;
                }
            }
            assert field != null;
            field.setAccessible(true);
            Object handler = field.get(projectView);
            Class<?>[] cs = clazz.getDeclaredClasses();
            for (Class<?> c : cs) {
                if ("MyAutoScrollFromSourceHandler".equals(c.getSimpleName())) {
                    Method m = c.getMethod("scrollFromSource");
                    m.setAccessible(true);
                    m.invoke(handler);
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("The plugin for \"Scroll from Source\" execute ERROR!", e);
        }
    }
}
