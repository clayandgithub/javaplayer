/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2015 Caprica Software Limited.
 */

package com.clayoverwind.javaplayer.action;

import com.clayoverwind.javaplayer.Application;
import static com.clayoverwind.javaplayer.Application.resources;

import javax.swing.*;

public final class Resource {

    private final String id;

    public static Resource resource(String id) {
        return new Resource(id);
    }

    private Resource(String id) {
        this.id = id;
    }

    public String name() {
        if (Application.INSTANCE.resources().containsKey(id)) {
            return Application.INSTANCE.resources().getString(id);
        } else {
            return null;
        }
    }

    public Integer mnemonic() {
        String key = id + ".mnemonic";
        if (Application.INSTANCE.resources().containsKey(key)) {
            return new Integer(Application.INSTANCE.resources().getString(key).charAt(0));
        }
        else {
            return null;
        }
    }

    public KeyStroke shortcut() {
        String key = id + ".shortcut";
        if (resources().containsKey(key)) {
            return KeyStroke.getKeyStroke(resources().getString(key));
        }
        else {
            return null;
        }
    }

    public String tooltip() {
        String key = id + ".tooltip";
        if (resources().containsKey(key)) {
            return resources().getString(key);
        }
        else {
            return null;
        }
    }

    public Icon menuIcon() {
        String key = id + ".menuIcon";
        if (resources().containsKey(key)) {
            return new ImageIcon(getClass().getResource("/icons/actions/" + resources().getString(key) + ".png"));
        }
        else {
            return null;
        }
    }

    public Icon buttonIcon() {
        String key = id + ".buttonIcon";
        if (resources().containsKey(key)) {
            return new ImageIcon(getClass().getResource("/icons/buttons/" + resources().getString(key) + ".png"));
        }
        else {
            return null;
        }
    }
}
