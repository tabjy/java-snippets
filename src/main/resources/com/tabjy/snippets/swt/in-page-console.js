(() => {
    const write = (content) => {
        output.value += content
        output.scrollTop = output.scrollHeight
    }

    const log = (...message) => {
        write("[LOG] " + message.join(' ') + '\n');
    }

    const error = (...message) => {
        if (message[0] instanceof Error) {
            return error(message[0].constructor.name + ': ' + message[0].message + '\n', '\t' + message[0].stack.split('\n').join('\n\t'));
        }
        write("[ERR] " + message.join(' ').split('\n').join('\n      ') + '\n');
    }

    const _consoleLog = console.log
    const _consoleError = console.error
    console.log = (...args) => {
        log(...args)
        _consoleLog(...args)
    }
    console.error = (...args) => {
        error(...args)
        _consoleError(...args)
    }
    window.onerror = (message, source, lineno, colno, err) => error(err)

    const exec = (function (cmd) {
        write("> " + cmd.trim().split('\n').join('\n  ') + '\n');
        try {
            let result = eval(cmd)
            if (typeof result === 'string') {
                result = '"' + result.split('\n').join('\n  ') + '"'
            }
            write("< " + result + '\n');
        } catch (e) {
            error(e)
        }

    }).bind(window)

    document.body.appendChild(document.createElement('hr'))
    
    const output = document.createElement('textarea')
    output.setAttribute('style', 'font-family: monospace')
    output.setAttribute('rows', '20')
    output.setAttribute('cols', '80')
    output.setAttribute('readonly', 'true')
    document.body.appendChild(output)
    document.body.appendChild(document.createElement('br'))

    const input = document.createElement('textarea')
    output.setAttribute('style', 'font-family: monospace')
    input.setAttribute('rows', '1')
    input.setAttribute('cols', '80')
    input.setAttribute('style', 'resize: none; overflow: hidden')
    {
        const resize = () => {
            input.style.height = "0px";
            input.style.height = (input.scrollHeight) + "px";
        }
        input.oninput = resize
        input.onkeydown = (e) => {
            e.keyCode === 13 && !e.shiftKey ? exec(input.value) : null
        }
        input.onkeyup = (e) => {
            e.keyCode === 13 && !e.shiftKey ? input.value = '' : null
            resize()
        }
    }
    document.body.appendChild(input)
    document.body.appendChild(document.createElement('br'))

    const execute = document.createElement('input')
    execute.type = 'button'
    execute.value = 'Execute'
    execute.onclick = () => exec(input.value)
    document.body.appendChild(execute)

    const clear = document.createElement('input')
    clear.type = 'button'
    clear.value = 'Clear'
    clear.onclick = () => output.value = ''
    document.body.appendChild(clear)
})()