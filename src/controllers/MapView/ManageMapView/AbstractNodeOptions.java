package controllers.MapView.ManageMapView;

import controllers.AbstractController;

/**
 * Created by mattm on 4/30/2017
 *
 */
public abstract class AbstractNodeOptions extends AbstractController {
    protected ManageMapSnackbarController parent;

    public AbstractNodeOptions(ManageMapSnackbarController parent) {
        super(parent);
    }

    @Override
    public void initData(Object[] data) {
        this.parent = (ManageMapSnackbarController) data[0];
    }

    public abstract void nodeChanged();
    public abstract void savePressed();
    public abstract void cancelPressed();
    public abstract boolean hasUnsavedChanges();
    public abstract String getRoomName();
}
