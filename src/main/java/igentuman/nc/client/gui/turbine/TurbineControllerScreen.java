package igentuman.nc.client.gui.turbine;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import igentuman.nc.client.gui.IVerticalBarScreen;
import igentuman.nc.client.gui.element.NCGuiElement;
import igentuman.nc.client.gui.element.bar.VerticalBar;
import igentuman.nc.client.gui.element.button.Checkbox;
import igentuman.nc.container.TurbineControllerContainer;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.Inventory;
import org.antlr.v4.runtime.misc.NotNull;;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static igentuman.nc.NuclearCraft.MODID;
import static igentuman.nc.handler.config.TurbineConfig.TURBINE_CONFIG;
import static igentuman.nc.util.TextUtils.applyFormat;

public class TurbineControllerScreen extends ContainerScreen<TurbineControllerContainer> implements IVerticalBarScreen {
    protected final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/turbine/controller.png");
    protected int relX;
    protected int relY;
    private int xCenter;

    public TurbineControllerContainer container()
    {
        return (TurbineControllerContainer) menu;
    }

    public List<NCGuiElement> widgets = new ArrayList<>();
    public Checkbox checkboxCasing;
    public Checkbox checkboxInterior;
    private VerticalBar energyBar;
    public TextComponent casingTootip = new TranslationTextComponent("");
    public TextComponent interiorTootip = new TranslationTextComponent("");;

    public TurbineControllerScreen(TurbineControllerContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        imageWidth = 176;
        imageHeight = 176;
    }

    protected void updateRelativeCords()
    {
        relX = (this.width - this.imageWidth) / 2;
        relY = (this.height - this.imageHeight) / 2;
        NCGuiElement.RELATIVE_X = relX;
        NCGuiElement.RELATIVE_Y = relY;
    }

    protected void init() {
        super.init();
        Minecraft mc = Minecraft.getInstance();
        updateRelativeCords();
        widgets.clear();
        checkboxCasing = new Checkbox(imageWidth-19, 80, this,  isCasingValid());
        checkboxInterior =  new Checkbox(imageWidth-32, 80, this,  isInteriorValid());
        energyBar = new VerticalBar.Energy(17, 16,  this, container().getMaxEnergy());
    }

    private boolean isInteriorValid() {
        return  container().isInteriorValid();
    }

    private boolean isCasingValid() {
        return  container().isCasingValid();
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        xCenter = getGuiLeft()-imageWidth/2;
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    private void renderWidgets(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        for(NCGuiElement widget: widgets) {
            widget.draw(matrix, mouseX, mouseY, partialTicks);
        }
        checkboxCasing.setChecked(isCasingValid()).draw(matrix, mouseX, mouseY, partialTicks);
        if(isCasingValid()) {
            checkboxCasing.setTooltipKey("multiblock.casing.complete");
        } else {
            checkboxCasing.setTooltipKey("multiblock.casing.incomplete");
        }
        checkboxCasing.addTooltip(casingTootip);

        checkboxInterior.setChecked(isInteriorValid() && isCasingValid()).draw(matrix, mouseX, mouseY, partialTicks);
        if(isInteriorValid() && isCasingValid()) {
            checkboxInterior.setTooltipKey("multiblock.interior.complete");
        } else {
            checkboxInterior.setTooltipKey("multiblock.interior.incomplete");
        }
        checkboxInterior.addTooltip(interiorTootip);
        if(isInteriorValid() && isCasingValid()) {
            checkboxInterior.addTooltip(new TranslationTextComponent("turbine.active.coils", container().getActiveCoils()));
            checkboxInterior.addTooltip(new TranslationTextComponent("turbine.blades.flow", container().getFlow()*TURBINE_CONFIG.BLADE_FLOW.get()));
        }
        energyBar.draw(matrix, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(@NotNull MatrixStack matrixStack, int mouseX, int mouseY) {
        drawCenteredString(matrixStack, font,  menu.getTitle(), imageWidth/2, titleLabelY, 0xffffff);
        if(isCasingValid()) {
        //    casingTootip = applyFormat(new TranslationTextComponent("reactor.size", getMultiblockHeight(), getMultiblockWidth(), getMultiblockDepth()), TextFormatting.GOLD);
        } else {
        //    casingTootip = applyFormat(new TranslationTextComponent(getValidationResultKey(), getValidationResultData()), TextFormatting.RED);
        }

        if(isCasingValid()) {
            if (isInteriorValid()) {
         //       interiorTootip = applyFormat(new TranslationTextComponent("reactor.fuel_cells", getFuelCellsCount()), TextFormatting.GOLD);

                if(container().hasRecipe() && !container().getEfficiency().equals("NaN")) {
                  //  drawString(matrixStack, font, new TranslationTextComponent("fission_reactor.efficiency", container().getEfficiency()), 36, 62, 0x8AFF8A);
                }
            } else {
               // interiorTootip = applyFormat(new TranslationTextComponent(getValidationResultKey(), getValidationResultData()), TextFormatting.RED);
            }
        }

        renderTooltips(matrixStack, mouseX-relX, mouseY-relY);
    }

    private Object getValidationResultData() {
        return container().getValidationResultData().toShortString();
    }

    private String getValidationResultKey() {
        return container().getValidationResultKey();
    }

    private int getMultiblockHeight() {
        return container().getHeight();
    }

    private int getMultiblockWidth() {
        return container().getWidth();
    }

    private int getMultiblockDepth() {
        return container().getDepth();
    }

    @Override
    protected void renderBg(@NotNull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bind(GUI);
        updateRelativeCords();
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
        renderWidgets(matrixStack, partialTicks, mouseX, mouseY);
    }

    private void renderTooltips(MatrixStack pMatrixStack, int pMouseX, int pMouseY) {

        for(NCGuiElement widget: widgets) {
           if(widget.isMouseOver(pMouseX, pMouseY)) {
/*               renderTooltip(pMatrixStack, widget.getTooltips(),
                       Optional.empty(), pMouseX, pMouseY);*/
           }
        }
        if(checkboxCasing.isMouseOver(pMouseX, pMouseY)) {
/*            renderTooltip(pMatrixStack, checkboxCasing.getTooltips(),
                    Optional.empty(), pMouseX, pMouseY);*/
        }
        if(checkboxInterior.isMouseOver(pMouseX, pMouseY)) {
/*            renderTooltip(pMatrixStack, checkboxInterior.getTooltips(),
                    Optional.empty(), pMouseX, pMouseY);*/
        }
        if(container().getMaxEnergy() > 0) {
            energyBar.clearTooltips();
            energyBar.addTooltip(new TranslationTextComponent("reactor.forge_energy_per_tick", container().energyPerTick()));
            if(energyBar.isMouseOver(pMouseX, pMouseY)) {
/*                renderTooltip(pMatrixStack, energyBar.getTooltips(),
                        Optional.empty(), pMouseX, pMouseY);*/
            }
        }
    }

    @Override
    public double getEnergy() {
        return 0;
    }

    @Override
    public double getHeat() {
        return 0;
    }

    @Override
    public double getCoolant() {
        return 0;
    }

    @Override
    public double getHotCoolant() {
        return 0;
    }
}
