package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.Operation;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationType;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;

import static com.github.shynixn.astraledit.bukkit.logic.lib.Utils.tryParseDouble;

/**
 * Created by Shynixn 2018.
 * <p>
 * Version 1.2
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2018 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class AnglesCommand implements PlayerCommand {
    private final Plugin plugin;
    private final SelectionManager manager;
    /**
     * Creates an instance of {@link AnglesCommand} which depends on
     * a {@link SelectionManager} and a {@link Plugin}
     *
     * @param manager SelectionManager
     */
    public AnglesCommand(SelectionManager manager, Plugin plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }
    /**
     * Executes the given command if the arguments match.
     *
     * @param player executing the command.
     * @param args   arguments.
     * @return True if this command was executed. False if the arguments do not match.
     */
    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if (args.length != 4 || !args[0].equalsIgnoreCase("angles") ||
            !tryParseDouble(args[1]) || !tryParseDouble(args[2]) || !tryParseDouble(args[3]) ||
            !Permission.ANGLES.hasPermission(player)) {
            return false;
        }

        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (!this.manager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                final Operation operation = new Operation(OperationType.ANGLES);
                operation.setOperationData(this.manager.getSelection(player).getBlockAngle());
                this.manager.getSelection(player).setBlockAngle(new EulerAngle(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
                this.manager.addOperation(player, operation);
            }
        });

        return true;
    }
}