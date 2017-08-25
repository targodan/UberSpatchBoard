/*
 * The MIT License
 *
 * Copyright 2017 Luca Corbatto.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.targodan.usb.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author corbatto
 */
public class IRCClientRegistry {
    private static final List<IRCClient> supportedClients = new ArrayList<>();
    private static boolean isInitialized = false;
    
    private static void init() {
        if(IRCClientRegistry.isInitialized) {
            return;
        }
        IRCClientRegistry.registerClient(new Hexchat());
        IRCClientRegistry.isInitialized = true;
    }
    
    public static void registerClient(IRCClient client) {
        IRCClientRegistry.supportedClients.add(client);
    }
    
    public static List<IRCClient> getSupportedClients() {
        IRCClientRegistry.init();
        return Collections.unmodifiableList(IRCClientRegistry.supportedClients);
    }
    
    public static IRCClient getIRCClientByName(String name) {
        IRCClientRegistry.init();
        return IRCClientRegistry.supportedClients.stream()
                .filter(client -> client.getName().equals(name))
                .findFirst().orElse(null);
    }
}
