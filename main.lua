local backgroundImage = love.graphics.newImage( "background.png" )
local targetImage = love.graphics.newImage( "target.png" )

function love.load()
    print( "loaded!" )
end

function love.draw()
    love.graphics.draw( backgroundImage )
    love.graphics.draw( targetImage )
end

function love.update()
    
end
