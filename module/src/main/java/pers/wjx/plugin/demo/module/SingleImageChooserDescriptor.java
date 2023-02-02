package pers.wjx.plugin.demo.module;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.wjx.plugin.demo.module.common.ModuleBundle;

import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.List;

/**
 * @author wjx
 */
public class SingleImageChooserDescriptor extends FileChooserDescriptor {
    private static final List<String> supportedExtensions = Arrays.asList(ImageIO.getReaderFormatNames());

    public SingleImageChooserDescriptor() {
        super(true, false, false, false, false, false);
    }

    @Override
    public String getTitle() {
        return ModuleBundle.INSTANCE.message("panda.photo.choose.title");
    }

    @Override
    public boolean isFileSelectable(@Nullable VirtualFile file) {
        return file != null && supportedExtensions.contains(file.getExtension());
    }

    @Override
    public void validateSelectedFiles(VirtualFile @NotNull [] files) throws Exception {
        if (files.length == 0) {
            return;
        }
        for (VirtualFile file : files) {
            if (file != null && !supportedExtensions.contains(file.getExtension())) {
                throw new Exception(ModuleBundle.INSTANCE.message("please.choose.image.file"));
            }
        }
    }
}
