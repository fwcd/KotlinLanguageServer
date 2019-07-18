package org.javacs.kt

import org.jetbrains.kotlin.cli.common.repl.*
import org.jetbrains.kotlin.cli.jvm.repl.*
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration as KotlinCompilerConfiguration
import org.jetbrains.kotlin.script.*
import com.intellij.openapi.util.*
import org.jetbrains.kotlin.cli.common.messages.*
import org.junit.*
import org.junit.Assert.*
import org.hamcrest.Matchers.*
import org.jetbrains.kotlin.load.java.JvmAbi
import java.net.URLClassLoader
import kotlin.script.templates.standard.ScriptTemplateWithArgs

class SimpleScriptTest {
    @Test
    @Ignore
    fun basicScript() {
        val scriptDef = KotlinScriptDefinition(Any::class)
        val repl = GenericReplEvaluator(listOf())
        val config = KotlinCompilerConfiguration()
        config.put(CommonConfigurationKeys.MODULE_NAME, JvmAbi.DEFAULT_MODULE_NAME)
        val compiler = GenericReplCompiler(scriptDef, config, MessageCollector.NONE)
        val compilerState = compiler.createState()
        val replState = repl.createState()

        var line = compiler.compile(compilerState, ReplCodeLine(1, 1, "val x = 1"))
        assertNotError(line)
        repl.eval(replState, line as ReplCompileResult.CompiledClasses)

        line = compiler.compile(compilerState, ReplCodeLine(2, 2, "x"))
        assertNotError(line)
        val result = repl.eval(replState, line as ReplCompileResult.CompiledClasses)

        when (result) {
            is ReplEvalResult.ValueResult -> println(result.value)
            is ReplEvalResult.UnitResult -> println('_')
        }
    }

    private fun assertNotError(result: ReplCompileResult) {
        if (result is ReplCompileResult.Error) {
            fail(result.message)
        }
    }
}

