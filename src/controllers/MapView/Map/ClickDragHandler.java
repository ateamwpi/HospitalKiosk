package controllers.MapView.Map;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created by dylan on 4/6/17.
 */
class ClickDragHandler implements EventHandler<MouseEvent> {

    private final EventHandler<MouseEvent> onDraggedEventHandler;

    private final EventHandler<MouseEvent> onClickedEventHandler;

    private boolean dragging = false;

    public ClickDragHandler(EventHandler<MouseEvent> onClickedEventHandler, EventHandler<MouseEvent> onDraggedEventHandler) {
        this.onDraggedEventHandler = onDraggedEventHandler;
        this.onClickedEventHandler = onClickedEventHandler;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            dragging = false;
        }
        else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
            dragging = true;
        }
        else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            //maybe filter on dragging (== true)
            onDraggedEventHandler.handle(event);
        }
        else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (!dragging) {
                onClickedEventHandler.handle(event);
            }
        }

    }
}
