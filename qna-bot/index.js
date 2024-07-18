require('dotenv').config();
let fs = require("fs"),
    path = require("path"),
    OpenAI = require("openai");

const openai = new OpenAI();

async function main() {
    const completion = await openai.chat.completions.create({
        messages: [
            { "role": "system", "content": "You are a helpful assistant." },
            { "role": "user", "content": "A student was asked a question - \"How did India get independence?\". He replied this - \"India got independence through non-violent movements of Gandhi and millions of freedom fighters. It took more than 200 years and lot of sacrifices of the patriots. But Netaji's azad hind army expidited the independence.\". What should be his score out of 10 for this answer? Just provide the numeric score value." }
        ],
        model: "gpt-3.5-turbo",
    });

    console.log(JSON.stringify(completion.choices[0]));
}
main();