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
package de.targodan.usb.io.processing;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * IRCMessage represents an IRC message including the timestamp, sender, channel
 * and content.
 * 
 * This class is immutable.
 * 
 * @author Luca Corbatto
 */
public class IRCMessage {
    protected final LocalDateTime timestamp;
    // FromUser will be "*" if it is an event, like "someone joined" or "someone quit".
    protected final String sender;
    protected final String channel;
    protected final String content;

    /**
     * Constructs an IRCMessage.
     * 
     * @param timestamp The time and date at which the message was sent.
     * @param sender The name of the user who sent the message.
     * @param channel The name of the channel in which the message was sent.
     * @param content The content of the message.
     */
    public IRCMessage(LocalDateTime timestamp, String sender, String channel, String content) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.channel = channel;
        this.content = content;
    }

    /**
     * Returns the time and date at which the message was sent.
     * @return the time and date at which the message was sent.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the name of the user who sent the message.
     * 
     * @return the name of the user who sent the message.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the name of the channel in which the message was sent.
     * 
     * @return the name of the channel in which the message was sent.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Returns the content of the message.
     * 
     * @return the content of the message.
     */
    public String getContent() {
        return content;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.timestamp);
        hash = 89 * hash + Objects.hashCode(this.sender);
        hash = 89 * hash + Objects.hashCode(this.channel);
        hash = 89 * hash + Objects.hashCode(this.content);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IRCMessage other = (IRCMessage) obj;
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.channel, other.channel)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        return true;
    }
}
