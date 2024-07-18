require('dotenv').config();
let fs = require("fs"),
    path = require("path"),
    OpenAI = require("openai");

const openai = new OpenAI();

const speechFile = path.resolve("./speech.mp3");

async function main() {
    const mp3 = await openai.audio.speech.create({
        model: "tts-1",
        voice: "nova",
        input: "शायरी के माध्यम से हम अपनी भावनाओं को न केवल व्यक्त कर सकते हैं, बल्कि दूसरों की भावनाओं को भी समझ सकते हैं। यह एक ऐसा सशक्त माध्यम है जो भाषा की",
    });
    console.log(speechFile);
    const buffer = Buffer.from(await mp3.arrayBuffer());
    await fs.promises.writeFile(speechFile, buffer);
}
main();