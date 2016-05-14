function [ r ] = degenfn( x )
%DEGENFN Summary of this function goes here
%   Detailed explanation goes here
    cols = size(x, 2);
    
    r = 0; % Non-degenerate sample by default. Now calculating.
    for i = 1 : cols
        for j = 1 : cols
            if i ~= j
                % Calculate distance between top and bottom part.
                distance = (x(1:2, i) - x(3:4, j));
                if norm(distance) < 99
                    r = 1; % Too close, degenerate sample.
                end
            end
        end
    end

end

