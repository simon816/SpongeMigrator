package com.simon816.migrator.config;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigValue<T> {

    private final boolean doesExist;
    private final T value;
    private final T def;

    public ConfigValue(ConfigurationNode node, Function<ConfigurationNode, T> getter, T defValue) {
        this.doesExist = !node.isVirtual();
        this.value = getter.apply(node);
        this.def = defValue;
    }

    public T getValue() {
        return this.value;
    }

    public boolean exists() {
        return this.doesExist;
    }

    public boolean isDefault() {
        return this.value.equals(this.def);
    }

    public void ifExists(Consumer<T> consumer) {
        if (this.doesExist) {
            consumer.accept(this.value);
        }
    }

    public void getOrDefault(Consumer<T> consumer) {
        consumer.accept(this.doesExist ? this.value : this.def);
    }

    public T getOrElse(T def) {
        return this.doesExist ? this.value : def;
    }

    public T getOrDefault() {
        return this.doesExist ? this.value : this.def;
    }

    public void ifNotDefault(Consumer<T> consumer) {
        if (!isDefault()) {
            consumer.accept(this.value);
        }
    }

    public static ConfigValue<Boolean> bool(ConfigurationNode node, boolean defValue) {
        return new ConfigValue<>(node, ConfigurationNode::getBoolean, defValue);
    }

    public static ConfigValue<String> string(ConfigurationNode node, String defValue) {
        return new ConfigValue<>(node, ConfigurationNode::getString, defValue);
    }

    public static ConfigValue<Long> numLong(ConfigurationNode node, long defValue) {
        return new ConfigValue<>(node, ConfigurationNode::getLong, defValue);
    }

    public static ConfigValue<Integer> numInt(ConfigurationNode node, int defValue) {
        return new ConfigValue<>(node, ConfigurationNode::getInt, defValue);
    }

    public static ConfigValue<Double> numDouble(ConfigurationNode node, double defValue) {
        return new ConfigValue<>(node, ConfigurationNode::getDouble, defValue);
    }

}
