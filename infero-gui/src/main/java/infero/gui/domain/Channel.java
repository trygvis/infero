package infero.gui.domain;

import infero.gui.domain.Channel.Properties;

import static infero.gui.domain.Channel.Properties.NAME;

/**
 */
public class Channel extends AbstractDomainObject<Properties> {
    public final Integer index;
    public final String indexText;
    private String name;

    public enum Properties {
        NAME,
    }

    private Channel(int index, String name) {
        this.index = index;
        this.indexText = Integer.toString(index);
        setName(name);
    }

    public static Channel getBlankChannel(int index) {
        return new Channel(index, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            name = "Channel " + (index + 1);
        }
        firePropertyChange(NAME, this.name, this.name = name);
    }
}
